package com.dutaduta.sketchme.oidc.jwt;

import com.dutaduta.sketchme.oidc.dto.UserArtistIdDTO;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;

/**
 * Jwt 생성 및 유효성 검증
 */
@Log4j2
@Component
public class JwtProvider {

    // 시크릿 키를 담는 변수
    private static String CACHED_SECRETKEY;

    // plain 시크릿 키를 담는 변수
    private static String SECRETKEY_PLAIN;
    @Value("${jwt.secretKey}")
    public void setPlainKey(String value) {
        SECRETKEY_PLAIN = value;
    }


    // access token 만료시간 30분
//    private static final Long ACCESS_TOKEN_VALID_TIME = Duration.ofMinutes(30).toMillis();
    private static final Long ACCESS_TOKEN_VALID_TIME = Duration.ofDays(1).toMillis();
    // refresh token 만료시간 2주
    private static final Long REFRESH_TOKEN_VALID_TIME = Duration.ofDays(14).toMillis();


    /**
     * plain -> 시크릿 키 변환
     * @return
     */
    private static String _getSecretKey() {
        log.debug("SECRETKEY_PLAIN : "+SECRETKEY_PLAIN);
        // Base64로 인코딩
        String keyBase64Encoded = Base64.getEncoder().encodeToString(SECRETKEY_PLAIN.getBytes());
        return keyBase64Encoded;
    }

    /**
     * 시크릿 키를 반환하는 method
     * @return
     */
    public static String getSecretKey() {
        if (CACHED_SECRETKEY == null) CACHED_SECRETKEY = _getSecretKey();
        return CACHED_SECRETKEY;
    }


    public static Long getUserId(String token, String secretKey) throws ExpiredJwtException {
        logIfTokenCanNotBeDecrypted(token, secretKey);
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);
    }

    public static Long getArtistId(String token, String secretKey) {
        logIfTokenCanNotBeDecrypted(token, secretKey);
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("artistId", Long.class);
    }

    private static void logIfTokenCanNotBeDecrypted(String token, String secretKey) {
        if(token ==null){
            log.error("액세스 토큰이 존재하지 않습니다.");
        } else if(secretKey ==null){
            log.error("시크릿 키가 존재하지 않습니다");
        }
    }

    public static boolean isRefreshToken(String token, String secretKey) {

        Header header = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getHeader();

        if (header.get("type").toString().equals("refresh")) {
            return true;
        }
        return false;
    }


    public static boolean isAccessToken(String token, String secretKey) {

        Header header = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getHeader();

        if (header.get("type").toString().equals("access")) {
            return true;
        }
        return false;
    }

    public static String createAccessToken(UserArtistIdDTO IDs, String secretKey) {
        return createJwt(IDs, secretKey, "access", ACCESS_TOKEN_VALID_TIME);
    }


    public static String createRefreshToken(UserArtistIdDTO IDs, String secretKey) {
        return createJwt(IDs, secretKey,"refresh", REFRESH_TOKEN_VALID_TIME);
    }

    public static String createJwt(UserArtistIdDTO IDs, String secretKey, String type, Long tokenValidTime) {
        Claims claims = Jwts.claims();
        claims.put("userId", IDs.getUser_id());
        claims.put("artistId", IDs.getArtist_id());

        return Jwts.builder()
                .setHeaderParam("type", type)
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact()
                ;
    }


    /**
     * 토큰으로부터 클레임을 만들고, 이를 통해 User 객체를 생성하여 Authentication 객체를 반환
     * @param token
     * @return
     */
//    public Authentication getAuthentication(String token) {
//        String username = Jwts.parser().setSigningKey(secret_key).parseClaimsJws(token).getBody().getSubject();
//        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }

    /**
     * http 헤더로부터 bearer 토큰(access or refresh)을 가져옴.
     * @param request
     * @return
     */
    public static String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
