package com.dutaduta.sketchme.oicd.exception;

public class ExpiredTokenException extends CodeException {
    public static final CodeException EXCEPTION = new ExpiredTokenException();

    private ExpiredTokenException() {
        super(GlobalErrorCode.TOKEN_EXPIRED);
    }
}
