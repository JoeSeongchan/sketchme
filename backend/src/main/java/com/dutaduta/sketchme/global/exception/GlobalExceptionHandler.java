package com.dutaduta.sketchme.global.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


//    @ExceptionHandler(BusinessException.class)
//    protected ResponseEntity<ResponseFormat<?>> handleException(Exception e) {
//        log.error("handleEntityNotFoundException", e);
//        return new ResponseEntity<>()
////        return new ResponseEntity(new ResponseFormat<>(CustomStatus.INVALID_INPUT_VALUE,
////                new TestResponseDTO(1, "error")), HttpStatus.OK);
//    }
}