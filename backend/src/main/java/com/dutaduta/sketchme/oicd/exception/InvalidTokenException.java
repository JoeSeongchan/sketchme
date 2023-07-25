package com.dutaduta.sketchme.oicd.exception;

public class InvalidTokenException extends CodeException {
    public static final CodeException EXCEPTION = new InvalidTokenException();

    private InvalidTokenException() {
        super(GlobalErrorCode.INVALID_TOKEN);
    }
}