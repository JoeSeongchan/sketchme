package com.dutaduta.sketchme.oicd.controller;

import com.dutaduta.sketchme.oicd.service.KakaoService;
import com.dutaduta.sketchme.oicd.jwt.JwtOIDCProvider;
import com.dutaduta.sketchme.oicd.dto.OIDCPublicKeysResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
public class KakaoController {
    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private JwtOIDCProvider jwtOIDCProvider;

    @ResponseBody
    @GetMapping("/api/oidc/kakao")
    public void kakaoCallback(@RequestParam String code) {
        // 내 어플리케이션 key
        final String REST_API_KEY = "2a805c77d6a3195034a21d25753a401f";
        final String ISS = "https://kauth.kakao.com";
        // 코드 성공적으로 받아옴
        log.info("code : " + code);

        // 인가 코드 이용해서 idToken 받아오기
        String idToken = kakaoService.getKakaoIdToken(code, REST_API_KEY);
        log.info("idToken : " + idToken);

        // idToken 페이로드(iss, aud, exp) 검증 후 kid 값 가져오기
        String kid = jwtOIDCProvider.getKidFromUnsignedTokenHeader(idToken, ISS, REST_API_KEY);
        log.info("kid : " + kid);

        // kid와 동일한 키값을 가진 공개키로 ID 토큰 유효성 검증


        // 검증된 토큰에서

    }

    // 공개키 목록 조회
//    @GetMapping("https://kauth.kakao.com/.well-known/jwks.json")
//    OIDCPublicKeysResponse getKakaoOIDCOpenKeys(){
//
//    };
}
