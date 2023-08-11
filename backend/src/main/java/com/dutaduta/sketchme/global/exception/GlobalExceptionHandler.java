package com.dutaduta.sketchme.global.exception;



import com.dutaduta.sketchme.global.ResponseFormat;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ResponseFormat<Object>> handleException(BusinessException e) {
        log.error("BusinessException", e);
        return ResponseFormat.fail(HttpStatus.valueOf(e.getStatusCode()),e.getMessage()).toEntity();
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