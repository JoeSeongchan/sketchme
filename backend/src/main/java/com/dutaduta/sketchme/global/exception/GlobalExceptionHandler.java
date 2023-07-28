package com.dutaduta.sketchme.global.exception;


import com.dutaduta.sketchme.global.CustomStatus;
import com.dutaduta.sketchme.global.ResponseFormat;
import com.dutaduta.sketchme.global.TestResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {



    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ResponseFormat<?>> handleException(Exception e) {
        log.error("handleEntityNotFoundException", e);
        return new ResponseEntity(new ResponseFormat<>(CustomStatus.INVALID_INPUT_VALUE,
                new TestResponseDTO(1,"error")), HttpStatus.OK);
    }
}