package com.dutaduta.sketchme.oidc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TokenResponseDTO {
    private String access_token;
    private String refresh_token;
}
