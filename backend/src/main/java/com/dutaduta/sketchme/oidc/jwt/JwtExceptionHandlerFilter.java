package com.dutaduta.sketchme.oidc.jwt;

import com.dutaduta.sketchme.global.ResponseFormat;
import com.dutaduta.sketchme.global.exception.TokenExpiredException;
import com.dutaduta.sketchme.global.exception.UnauthorizedException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
//@Profile({"local","dev","prod"})
@Profile({"dev","prod"})
@Component
public class JwtExceptionHandlerFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{
        try{
            filterChain.doFilter(request, response);
        } catch(TokenExpiredException e) {
            setErrorResponse(response, e.getStatusCode(), e.getMessage());
        } catch (UnauthorizedException e) {
            setErrorResponse(response, e.getStatusCode(), e.getMessage());
        }
    }
    private void setErrorResponse(
            HttpServletResponse response,
            int statusCode,
            String message
    ) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(objectMapper.writeValueAsString(ResponseFormat.fail(HttpStatus.valueOf(statusCode),message)));
    }
}
