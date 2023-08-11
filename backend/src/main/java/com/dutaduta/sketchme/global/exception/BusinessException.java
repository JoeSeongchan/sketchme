package com.dutaduta.sketchme.global.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private int statusCode;

    public BusinessException() {
    }

    public BusinessException(int statusCode, String message){
        super(message);
        this.statusCode=statusCode;
    }
}
