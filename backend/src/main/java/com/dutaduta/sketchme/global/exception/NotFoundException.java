package com.dutaduta.sketchme.global.exception;

public class NotFoundException extends BusinessException {
    public NotFoundException(String message){
        super(404, message);
    }
}
