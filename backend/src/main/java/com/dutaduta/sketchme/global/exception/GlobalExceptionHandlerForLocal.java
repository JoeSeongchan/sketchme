package com.dutaduta.sketchme.global.exception;



import com.dutaduta.sketchme.global.ResponseFormat;
import com.dutaduta.sketchme.mattermost.NotificationManager;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
@Profile({"local","test"})
public class GlobalExceptionHandlerForLocal {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ResponseFormat<Object>> handleException(BusinessException e, HttpServletRequest request) {
        log.error("BusinessException", e);
        return ResponseFormat.fail(HttpStatus.valueOf(e.getStatusCode()), e.getMessage()).toEntity();
    }


    private String getParams(HttpServletRequest req) {
        StringBuilder params = new StringBuilder();
        Enumeration<String> keys = req.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            params.append("- ").append(key).append(" : ").append(req.getParameter(key)).append("\n");
        }
        return params.toString();
    }

    @ExceptionHandler(TokenExpiredException.class)
    protected  ResponseEntity<ResponseFormat<Map<String, String>>> handleTokenExpiredException(TokenExpiredException e) {
        log.error("TokenExpiredException", e);
        Map<String, String> customCode = new HashMap<>();
        customCode.put("customCode", "E3003");
        return  ResponseFormat.fail(customCode, HttpStatus.UNAUTHORIZED, e.getMessage()).toEntity();
    }

    @ExceptionHandler(ExpiredJwtException.class)
    protected ResponseEntity<ResponseFormat<Map<String, String>>> handleExpiredJwtException(ExpiredJwtException e) {
        log.error("ExpiredJwtException", e);
        Map<String, String> customCode = new HashMap<>();
        customCode.put("customCode", "E3003");
        return ResponseFormat.fail(customCode, HttpStatus.UNAUTHORIZED,"토큰 유효 기한이 지났습니다.").toEntity();
    }
}