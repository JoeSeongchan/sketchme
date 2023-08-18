package com.dutaduta.sketchme.oidc.jwt;

import com.dutaduta.sketchme.global.exception.BusinessException;
import com.dutaduta.sketchme.global.exception.UnauthorizedException;
import com.dutaduta.sketchme.oidc.dto.OIDCDecodePayloadDTO;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

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
            throw new UnauthorizedException("유효기간이 지난 토큰입니다.");
        } catch (Exception e) {
            log.error(e.toString());
            throw new RuntimeException();
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


    /**
     * OIDCDecodePayload 를 가져온다. 스펙이라 공통으로 사용할 수 있다.
     * @param token
     * @param modulus
     * @param exponent
     * @return
     */
    public OIDCDecodePayloadDTO getOIDCTokenBody(String token, String modulus, String exponent) {
        Claims body = getOIDCTokenJws(token, modulus, exponent).getBody();
        return new OIDCDecodePayloadDTO(
                body.getIssuer(),
                body.getAudience(),
                body.getSubject(),
                body.get("email", String.class),
                body.get("nickname", String.class),
                body.get("picture", String.class));
    }

    /**
     * 공개키로 토큰 검증을 시도한다.
     * @param token
     * @param modulus
     * @param exponent
     * @return
     */
    public Jws<Claims> getOIDCTokenJws(String token, String modulus, String exponent) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getRSAPublicKey(modulus, exponent))
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            log.error(e.toString());
            throw new UnauthorizedException("만료된 토큰입니다.");
        } catch (Exception e) {
            log.error(e.toString());
            throw new UnauthorizedException("유효하지 않은 토큰입니다.");
        }
    }

    /**
     * n ,e 값으로 Rsa 퍼블릭 키를 연산 할 수 있다.
     * @param modulus
     * @param exponent
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    private Key getRSAPublicKey(String modulus, String exponent)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] decodeN = Base64.getUrlDecoder().decode(modulus);
        byte[] decodeE = Base64.getUrlDecoder().decode(exponent);
        BigInteger n = new BigInteger(1, decodeN);
        BigInteger e = new BigInteger(1, decodeE);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n, e);
        return keyFactory.generatePublic(keySpec);
    }
}
