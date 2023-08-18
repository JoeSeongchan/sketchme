package com.dutaduta.sketchme.oidc.jwt;

import com.dutaduta.sketchme.global.exception.TokenExpiredException;
import com.dutaduta.sketchme.global.exception.UnauthorizedException;
import com.dutaduta.sketchme.oidc.domain.TokenObject;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.security.sasl.AuthenticationException;
import java.io.IOException;
import java.util.List;


@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider JwtProvider;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getServletPath(); // 프로젝트 아래 경로만 가져옴
//        log.info(path);

        // 로그인, 그림/작가 검색, 작가 갤러리에서 필요한 api일 경우 jwt 토큰 검사 생략하고 다음 필터 단계로 넘어감
        if (
//                true //이거 다시 바꿔줘야함 테스트용도로 냅둔거
                path.startsWith("/oidc") || path.startsWith("/search")
                        || path.startsWith("/artist/info") || path.startsWith("/artist/desc")
                        || path.startsWith("/category/list") || path.startsWith("/display")
                        || path.startsWith("/download") || path.startsWith("/kakao.html")
                        || path.startsWith("/ws")
        ) {
//            log.info("JWT filter - doing Login (filter pass~~)");
            filterChain.doFilter(request, response);
            return;
        }

        String secretKey = JwtProvider.getSecretKey();
        TokenObject tokenObject = new TokenObject(JwtProvider.resolveToken(request));

//        log.info("token : " + tokenObject.getToken());

        //토큰 없거나, 유효하지 않은 경우
        if(tokenObject.getToken() == null) {
//            log.info("No Token");
            throw new UnauthorizedException("토큰이 존재하지 않습니다.");
        }
        if(!tokenObject.validateToken(secretKey)) {
            throw new TokenExpiredException("토큰 유효 기한이 지났습니다.");
        }

        // redis에 access token이 있다는 것은 유효기간은 남았지만 로그아웃된 토큰이라는 것
        log.info("logout token : " + redisTemplate.opsForValue().get(tokenObject.getToken()));
        if(JwtProvider.isAccessToken(tokenObject.getToken(), secretKey) && redisTemplate.opsForValue().get(tokenObject.getToken())!=null) {
            log.info("logout token");
            throw new UnauthorizedException("이미 로그아웃된 토큰입니다.");
        }

        // Token에서 UserId 꺼내기
        Long userId = JwtProvider.getUserId(tokenObject.getToken(), secretKey);
        log.info("userId : {}", userId);


        // 위에서 만료됐는지 확인했기 때문에 따로 만료확인 필요 없음
        // 리프레쉬 토큰이 유효한지와 path 정보를 통해 확인이 끝났기 때문에 컨트롤러에서는 바로 토큰 재발행해주고 보내주면 됨

        // 재발급 요청 api인데, access token을 전달했을 경우
        if (path.startsWith("/token") && JwtProvider.isAccessToken(tokenObject.getToken(), secretKey)) {
            throw new UnauthorizedException("Access Token이 아니라 Refresh Token을 주세요.");
        }

        // 재발급 요청이 아닌데 refresh token을 전달했을 경우
        if(!path.startsWith("/token") && JwtProvider.isRefreshToken(tokenObject.getToken(), secretKey)) {
            throw new UnauthorizedException("Refresh Token이 아니라 Access Token을 주세요.");
        }

        // SecurityContext 안에 Authentication 객체가 존재하는지의 유무를 체크해서 인증여부를 결정
        // 권한 부여
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, null, List.of(new SimpleGrantedAuthority("USER")));

        // Detail을 넣어줌
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken); // Spring context에 저장
        log.info("[+] Token in SecurityContextHolder");

        // 다음 필터 단계로 넘어감
        filterChain.doFilter(request, response);
    }
}
