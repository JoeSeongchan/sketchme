package com.dutaduta.sketchme.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ArtistInfoRequest {
    private String nickname;

    private Long[] hashtags;
}
