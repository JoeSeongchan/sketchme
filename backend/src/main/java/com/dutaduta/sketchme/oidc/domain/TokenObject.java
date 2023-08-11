package com.dutaduta.sketchme.oidc.domain;

import com.dutaduta.sketchme.oidc.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;


@AllArgsConstructor
@Getter
public class TokenObject {

    private String token;


    public Long getRestTime() {
        Date expiration = Jwts.parser().setSigningKey(JwtProvider.getSecretKey()).parseClaimsJws(this.token).getBody().getExpiration();

        // 현재 시간
        long now = new Date().getTime();

        return (expiration.getTime() - now);
    }

    /**
     * 토큰의 유효성 + 만료일자 확인
     * @param secretKey
     * @return
     */
    public boolean validateToken(String secretKey) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(this.token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return false;
        }
    }
}
