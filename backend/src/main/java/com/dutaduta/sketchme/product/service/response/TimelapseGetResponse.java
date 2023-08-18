package com.dutaduta.sketchme.product.service.response;

import com.dutaduta.sketchme.product.domain.Picture;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TimelapseGetResponse {
    private String timelapseUrl;
    private String thumbnailUrl;

    public static TimelapseGetResponse of(Picture picture){
        return TimelapseGetResponse.builder()
                .timelapseUrl(picture.getMeeting().getTimelapse().getUrl())
                .thumbnailUrl(picture.getMeeting().getTimelapse().getThumbnailUrl()).build();
    }
}
