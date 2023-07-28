package com.dutaduta.sketchme.global;

import lombok.Getter;

@Getter
public enum CustomStatus {


    //예시. 협의 후 변경해야함
    OK(200, "C001", "Success"),
    // Common
    INVALID_INPUT_VALUE(400, "E001", " Invalid Input Value"),
    METHOD_NOT_ALLOWED(405, "E002", " Invalid 뭐시기"),
    HANDLE_ACCESS_DENIED(403, "E003", "Access is Denied"),

    // Member
    EMAIL_DUPLICATION(400, "E004", "Email is Duplication"),
    LOGIN_INPUT_INVALID(400, "E05", "Login input is invalid"),

            ;
    private final String customCode;
    private final String message;
    private int httpStatusCode;

    CustomStatus(final int httpStatusCode, final String customCode, final String message) {
        this.httpStatusCode= httpStatusCode;
        this.customCode= customCode;
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.httpStatusCode);
    }
}