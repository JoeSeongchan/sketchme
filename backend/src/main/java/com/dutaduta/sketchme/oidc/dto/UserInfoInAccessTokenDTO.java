package com.dutaduta.sketchme.oidc.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoInAccessTokenDTO {
    private long userId;
    private long artistId;
}
