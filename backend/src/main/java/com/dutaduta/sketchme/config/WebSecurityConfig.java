package com.dutaduta.sketchme.config;

import com.dutaduta.sketchme.oidc.jwt.JwtAuthenticationFilter;
import com.dutaduta.sketchme.oidc.jwt.JwtExceptionHandlerFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity // 현재 클래스를 스프링 필터체인에 등록
@Log4j2
@RequiredArgsConstructor
//@Profile({"local","dev","prod"})
@Profile({"dev","prod"})
public class WebSecurityConfig {

    private final com.dutaduta.sketchme.oidc.jwt.JwtProvider JwtProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtExceptionHandlerFilter jwtExceptionHandlerFilter;

    /**
     * 인가가 필요한 리소스 설정 (특정 경로에 대한 설정 변경)
     */
    @Bean
//    @Profile({"local","dev","prod"})
    @Profile({"dev","prod"})
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /** API 개발을 위해 Spring Security 비활성화 */
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                // CORS
                .cors(Customizer.withDefaults()) // corsConfigurationSource라는 이름으로 등록된 Bean을 이용
                // 접근권한 설정 (요청에 의해 보안 검사 시작)
                .authorizeRequests(requests -> {
                    requests.requestMatchers(new AntPathRequestMatcher("/**")).permitAll(); // 로그인 경로는 모든 사용자에게 허락
//                    requests.requestMatchers(new AntPathRequestMatcher("/api/*")).authenticated(); // 그 외에는 인증된 사용자만 허락
                })
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // JWT 쓸 때 사용
                )
                .addFilterBefore(jwtExceptionHandlerFilter, UsernamePasswordAuthenticationFilter.class) // ExceptionHandler 필터가 앞에 와야 함!
                .addFilterBefore(new JwtAuthenticationFilter(JwtProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class) // UsernamePasswordAuthenticationFilter 앞에 JwtFilter 추가
        ;


        return http.build();
    }

    @Bean
//    @Profile({"local","dev","prod"})
    @Profile({"dev","prod"})
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // cross-origin 요청이 허가되는 url
        configuration.setAllowedOrigins(Arrays.asList("https://sketchme.ddns.net", "http://localhost:3000", "http://25.30.154.219:3000", "http://25.4.138.123:3000"));
//        configuration.setAllowedOrigins(List.of("*"));
        //허용할 헤더 설정
        configuration.addAllowedHeader("*");
        //허용할 http method
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}