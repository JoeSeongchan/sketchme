package com.dutaduta.sketchme.global.exception;

public class InternalServerErrorException extends BusinessException{
    public InternalServerErrorException(String message){
        super(500, message);
    }
}
