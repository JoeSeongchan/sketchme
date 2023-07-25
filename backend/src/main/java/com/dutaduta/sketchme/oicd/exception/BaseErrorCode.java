package com.dutaduta.sketchme.oicd.exception;

import com.dutaduta.sketchme.oicd.dto.ErrorReason;

public interface BaseErrorCode {
    public ErrorReason getErrorReason();

    String getExplainError() throws NoSuchFieldException;
}
