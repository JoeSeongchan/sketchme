package com.dutaduta.sketchme.videoconference.service.response;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ConnectionCreateResponse {
    private final String token;
}
