package com.dutaduta.sketchme.global;

import lombok.*;

@Getter
@ToString
public class ResponseFormat<T> {

    private String customCode;
    private String message;
    private final T data;

    private ResponseFormat(T data) {
        this.customCode =  CustomStatus.OK.getCustomCode();
        this.message = CustomStatus.OK.getMessage();
        this.data = data;
    }

    private ResponseFormat(T data, String message) {
        this.customCode =  CustomStatus.OK.getCustomCode();
        this.message = message;
        this.data = data;
    }

    public ResponseFormat(CustomStatus code, T data) {
        this.customCode = code.getCustomCode();
        this.message = code.getMessage();
        this.data = data;
    }
}
