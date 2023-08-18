package com.dutaduta.sketchme.videoconference.service.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
@Builder
public class ConnectionGetResponse {
    private String token;
}
