package com.dutaduta.sketchme.oidc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public class OIDCDecodePayloadDTO {
    /** issuer ex https://kauth.kakao.com */
    private String iss;
    /** client id */
    private String aud;
    /** oauth provider account unique id */
    private String sub;

    private String email;

    private String nickname;

    private String profile_img_url;
}