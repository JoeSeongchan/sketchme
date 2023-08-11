package com.dutaduta.sketchme.global.exception;

public class TokenExpiredException extends BusinessException {

    private String customCode;
    private String message;

    public TokenExpiredException(String message){
        this.customCode = "E3003";
        this.message = message;
    }
}
