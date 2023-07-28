package com.dutaduta.sketchme.global;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@ToString
@Builder
public class ResponseFormat<T> {

    private final String customCode;
    private final String message;
    @JsonIgnore
    private final int httpStatusCode;
    private final T data;

    public static <T> ResponseFormat<T> success(T data) {
        return ResponseFormat.<T>builder()
                .customCode(CustomStatus.OK.getCustomCode())
                .httpStatusCode(CustomStatus.OK.getHttpStatusCode())
                .message(CustomStatus.OK.getMessage())
                .data(data).build();
    }

    public static <T> ResponseFormat<T> successWithCustomMsg(T data, String customMsg) {
        return ResponseFormat.<T>builder()
                .customCode(CustomStatus.OK.getCustomCode())
                .httpStatusCode(CustomStatus.OK.getHttpStatusCode())
                .message(customMsg)
                .data(data).build();
    }

    public static <T> ResponseFormat<T> fail(T data, CustomStatus code) {
        return ResponseFormat.<T>builder()
                .customCode(code.getCustomCode())
                .httpStatusCode(code.getHttpStatusCode())
                .message(code.getMessage())
                .data(data).build();
    }

    public ResponseEntity<ResponseFormat<T>> toEntity() {
        return new ResponseEntity<>(this, HttpStatus.valueOf(httpStatusCode));
    }
}
