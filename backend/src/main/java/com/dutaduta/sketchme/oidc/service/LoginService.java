package com.dutaduta.sketchme.oidc.service;

import com.dutaduta.sketchme.file.dto.UploadResponse;
import com.dutaduta.sketchme.file.service.FileService;
import com.dutaduta.sketchme.global.ResponseFormat;
import com.dutaduta.sketchme.member.domain.OAuthType;
import com.dutaduta.sketchme.member.domain.User;
import com.dutaduta.sketchme.member.dao.UserRepository;
import com.dutaduta.sketchme.oidc.dto.OIDCDecodePayloadDTO;
import com.dutaduta.sketchme.oidc.dto.OIDCPublicKeyDTO;
import com.dutaduta.sketchme.oidc.dto.TokenResponseDTO;
import com.dutaduta.sketchme.oidc.dto.TokenResponseDTO;
import com.dutaduta.sketchme.oidc.dto.UserArtistIdDTO;
import com.dutaduta.sketchme.oidc.jwt.JwtOIDCProvider;
import com.dutaduta.sketchme.oidc.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class LoginService {

    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private JwtOIDCProvider jwtOIDCProvider;

    private final UserRepository userRepository;

    private final RedisTemplate<String, String> redisTemplate;

    private final Long refreshTokenValidTime = Duration.ofDays(14).toMillis();

    private final FileService fileService;

    // 내 어플리케이션 key
    @Value("${kakao.rest-api-key}")
    private String REST_API_KEY;

    @Value("${kakao.iss}")
    private String ISS;

    /**
     * 카카오 로그인 전체 과정 (인가코드 받은 이후 ~ access, refresh 토큰 반환)
     *
     * @param code
     * @return
     */
    public TokenResponseDTO KakaoLogin(String code) {
        // 코드 성공적으로 받아옴
        log.info("code : " + code);

        // 인가 코드 이용해서 idToken 받아오기
        String idToken = kakaoService.getKakaoIdToken(code, REST_API_KEY);
        log.info("idToken : " + idToken);

        // idToken 페이로드(iss, aud, exp) 검증 후 kid 값 가져오기
        String kid = jwtOIDCProvider.getKidFromUnsignedTokenHeader(idToken, ISS, REST_API_KEY);
        log.info("kid : " + kid);

        // 공개키 목록 조회 (JSON으로 받은 것 객체로 파싱해서 가져옴)
        List<OIDCPublicKeyDTO> OIDCPublicKeys = kakaoService.getKakaoOIDCOpenKeys();

        // kid와 동일한 키값을 가진 공개키로 ID 토큰 유효성 검증
        // kid와 동일한 키값 가진 공개키 가져오기
        OIDCPublicKeyDTO oidcPublicKeyDto = OIDCPublicKeys.stream()
                .filter(o -> o.getKid().equals(kid))
                .findFirst()
                .orElseThrow();

        // 검증이 된 토큰에서 바디를 꺼내온다.
        OIDCDecodePayloadDTO payload = jwtOIDCProvider.getOIDCTokenBody(idToken, oidcPublicKeyDto.getN(), oidcPublicKeyDto.getE());
        log.info("OIDCDecodePayload : " + payload.toString());

        // 회원가입 된 회원인지 찾기 -> 회원 정보(sub) 없다면 회원가입 처리
        // user 테이블 - oauth_id, o_auth_type, (email), nickname, profile_img_url, is_logined
        UserArtistIdDTO UserArtistIdDTO = signUp(payload, OAuthType.KAKAO);
        log.info(UserArtistIdDTO.toString());

        // 로그인 처리를 위해 jwt 토큰 생성 (access token, refresh token)
        String secretKey = JwtProvider.getSecretKey();
        log.info("secretKey : " + secretKey);
        String refreshToken = JwtProvider.createRefreshToken(UserArtistIdDTO, secretKey);
        String accessToken = JwtProvider.createAccessToken(UserArtistIdDTO, secretKey);
        log.info("SketchMe refreshToken : " + refreshToken);
        log.info("SketchMe accessToken : " + accessToken);

        // refresh token은 redis에 저장
        TokenResponseDTO tokenResponseDto = new TokenResponseDTO(accessToken, refreshToken);

        String redisSub = UserArtistIdDTO.getUser_id().toString();
        // Redis에 저장 - 만료 시간 설정을 통해 자동 삭제 처리
        redisTemplate.opsForValue().set(
                redisSub,
                refreshToken,
                refreshTokenValidTime,
                TimeUnit.MILLISECONDS
        );

//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("Authorization", "Bearer " + accessToken); // 인증타입 Bearer

        // client에게 access token, refresh token 반환
        return tokenResponseDto;

        // 로그인/회원가입은 끝!!
    }

    /**
     * OICD로 받아온 사용자 정보를 토대로 회원가입이 된 유저인지 확인
     * 회원가입이 안 되어 있다면 회원가입 처리하기
     * 카카오, 구글 상관없이 전부 진행하는 과정.
     * @param payload   유저 식별 정보 (카카오 or 구글에서의 회원번호), 사용자 이메일 등 ID TOKEN의 body
     * @param oauthType KAKAO / GOOGLE
     */

    public UserArtistIdDTO signUp(OIDCDecodePayloadDTO payload, OAuthType oauthType) {

        // sub로 판단하려면.. 구글이랑 겹치지는 않나??
        // => oauth_id, o_auth_type 같이 조회!

        // 회원인지 여부 판단
        User signedUser = userRepository.findByOauthIdAndOauthTypeAndIsDeleted(payload.getSub(), oauthType, false);
        Long artist_id = 0L;

        // 회원 아니면 회원가입 처리
        if(signedUser == null) {
            log.info("SignUp NO");
            User user = User.builder()
                    .oauthId(payload.getSub())
                    .oauthType(oauthType)
                    .email(payload.getEmail())
                    .nickname(payload.getNickname())
                    .isLogined(true).build();
            log.info(user.toString());
            Long userID = userRepository.save(user).getId();

            // 프로필 이미지를 우리 서버에 저장해줘야 함.
            UploadResponse dto = fileService.saveImageUrl(payload.getProfile_img_url(), userID);
//            user = userRepository.findById(userID).orElseThrow(BusinessException::new);
            user.updateImgUrl(dto.getImageURL(), dto.getThumbnailURL());


            return UserArtistIdDTO.builder().user_id(user.getId()).artist_id(artist_id).build();
        }

        // 로그인 여부 true로 전환
        log.info("SignUp YES");
        signedUser.updateIsLogined(true);
        userRepository.save(signedUser);
        log.info(signedUser.toString());
        if(signedUser.isDebuted() && signedUser.getArtist() != null) artist_id = signedUser.getArtist().getId();
        return UserArtistIdDTO.builder().user_id(signedUser.getId()).artist_id(artist_id).build();
    }

    /**
     *
     * @param request
     * @return
     */
    public ResponseFormat<?> regenerateToken(HttpServletRequest request) {
        log.info("----------access token REGENERATE-----------");

        // 헤더에서 refresh 토큰 가져오기 (검증은 filter에서 완료)
        String refreshToken = JwtProvider.resolveToken(request);
        String secretKey = JwtProvider.getSecretKey();

        // 검증된 refresh token에서 userId & artistId 가져오기
        Long userId = JwtProvider.getUserId(refreshToken, secretKey);
        Long artistId = JwtProvider.getArtistId(refreshToken, secretKey);
        log.info("userId : "+userId);
        log.info("artistId : "+artistId);


        // redis에 저장된 refresh token 값을 가져오기.
        String redis_refreshToken = redisTemplate.opsForValue().get(userId.toString());
        log.info("redis_refreshToken : " + redis_refreshToken);

        // 1) refresh token 만료됐다면 redis에 없을거임(null). access token 재발급 불가
        // 2) redis에서 가져온 refresh token과 사용자에게 받은 refresh token이 다르면 재발급 불가
        if(redis_refreshToken == null || !redis_refreshToken.equals(refreshToken)) {
            return ResponseFormat.fail( HttpStatus.UNAUTHORIZED,"refresh 토큰이 만료되었거나, 같지 않습니다!");
        }

        // 같다면, access token 재발급
        String accessToken = JwtProvider.createAccessToken(new UserArtistIdDTO(userId, artistId), secretKey);
        Map<String, String> result = new HashMap<>();
        result.put("access_token", accessToken);

        return ResponseFormat.success(result);
    }
}
