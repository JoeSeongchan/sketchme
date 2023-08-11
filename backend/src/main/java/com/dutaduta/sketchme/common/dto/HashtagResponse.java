package com.dutaduta.sketchme.common.dto;

import com.dutaduta.sketchme.common.domain.Hashtag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class HashtagResponse {

    @NotNull
    private Long hashtagID;

    @NotBlank
    private String name;

    public static HashtagResponse of(Hashtag hashtag) {
        return HashtagResponse.builder()
                .hashtagID(hashtag.getId())
                .name(hashtag.getName()).build();
    }
}
