package com.dutaduta.sketchme.product.dto;

import com.dutaduta.sketchme.common.dto.HashtagResponse;
import com.dutaduta.sketchme.file.dto.ImgUrlResponse;
import com.dutaduta.sketchme.product.domain.Picture;
import com.dutaduta.sketchme.product.service.response.TimelapseGetResponse;
import com.dutaduta.sketchme.review.domain.Review;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class MyPictureResponse {

    @NotNull
    private Long id;

    @NotNull
    private ImgUrlResponse pictureImgUrl;

    private TimelapseGetResponse pictureTimelapseUrl;

    public static MyPictureResponse of(Picture picture) {
        return MyPictureResponse.builder()
                .id(picture.getId())
                .pictureImgUrl(ImgUrlResponse.builder().imgUrl(picture.getUrl()).thumbnailUrl(picture.getThumbnailUrl()).build())
                .pictureTimelapseUrl(TimelapseGetResponse.of(picture)).build();
    }
}
