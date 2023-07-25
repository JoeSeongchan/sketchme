package com.dutaduta.sketchme.oicd.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class KakaoUserInfo {

    private Long id;
    //    private String nickname;
    private String profileImgUrl;
}