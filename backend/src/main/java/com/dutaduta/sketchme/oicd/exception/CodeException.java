package com.dutaduta.sketchme.oicd.exception;

import com.dutaduta.sketchme.oicd.dto.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CodeException extends RuntimeException{
    private BaseErrorCode errorCode;

    public ErrorReason getErrorReason() {
        return this.errorCode.getErrorReason();
    }
}
