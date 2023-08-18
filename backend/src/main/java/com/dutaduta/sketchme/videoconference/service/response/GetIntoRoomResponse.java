package com.dutaduta.sketchme.videoconference.service.response;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class GetIntoRoomResponse {
    // 작가와 고객 모두 기본적으로 필요로 하는 연결 토큰
    private final String token;
}
