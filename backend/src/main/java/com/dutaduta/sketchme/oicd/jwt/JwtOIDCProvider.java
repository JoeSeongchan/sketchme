package com.dutaduta.sketchme.oicd.jwt;

import com.dutaduta.sketchme.oicd.exception.ExpiredTokenException;
import com.dutaduta.sketchme.oicd.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * ID 토큰에서 헤더, 페이로드, 서명을 분리한 후 헤더, 페이로드 반환
 */
@Component
@Log4j2
public class JwtOIDCProvider {
    private final String KID = "kid";

    /**
     * 아직 인증되지 않은 토큰. 인증을 위해 공개키 목록에서 사용할 kid 찾아서 반환
     * @param token ID 토큰
     * @param iss 이슈어 = 카카오(https://kauth.kakao.com)
     * @param aud 서비스 앱 키
     * @return
     */
    public String getKidFromUnsignedTokenHeader(String token, String iss, String aud) {
        return (String) getUnsignedTokenClaims(token, iss, aud).getHeader().get(KID);
    }

    /**
     * 페이로드 검증
     * base64로 디코딩해서 iss, aud, exp 확인
     * @param token ID 토큰
     * @param iss  이슈어 = 카카오(https://kauth.kakao.com)
     * @param aud 서비스 앱 키
     * @return
     */
    private Jwt<Header, Claims> getUnsignedTokenClaims(String token, String iss, String aud) {
        try {
            // JwtParser : A parser for reading JWT strings
            return Jwts.parserBuilder() // Jwt를 파싱할 builder 만들기
                    .requireAudience(aud)  //aud(내 앱key)가 같은지 확인
                    .requireIssuer(iss) //iss(이슈어)가 카카오인지 확인
                    .build() //JwtParser 빌드
                    .parseClaimsJwt(getUnsignedToken(token)); // ID 토큰에서 서명 분리해서 헤더, 페이로드만 파싱하기
        } catch (ExpiredJwtException e) { //파싱하면서 만료된 토큰인지 확인
            throw ExpiredTokenException.EXCEPTION;
        } catch (Exception e) {
            log.error(e.toString());
            throw InvalidTokenException.EXCEPTION;
        }
    }

    /**
     * ID 토큰 분리을 헤더, 페이로드, 서명 부분으로 분리하고 헤더, 페이로드 반환
     * @param token  ID 토큰
     * @return 헤더, 페이로드
     * @throws Exception
     */
    private String getUnsignedToken(String token) throws Exception {
        String[] splitToken = token.split("\\.");
        if (splitToken.length != 3) throw new Exception();
        return splitToken[0] + "." + splitToken[1] + "."; // 헤더, 페이로드 반환
    }
}
