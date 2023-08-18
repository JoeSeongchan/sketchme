package com.dutaduta.sketchme.file.dto;

import com.dutaduta.sketchme.product.domain.Picture;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class ImgUrlResponse {
    @NotBlank
    private String imgUrl;

    @NotBlank
    private String thumbnailUrl;

    public static ImgUrlResponse of(UploadResponse uploadResponse) {
        return ImgUrlResponse.builder()
                .imgUrl(uploadResponse.getImageURL())
                .thumbnailUrl(uploadResponse.getThumbnailURL()).build();
    }

    public static ImgUrlResponse of(Picture picture) {
        return ImgUrlResponse.builder()
                .imgUrl(picture.getUrl())
                .thumbnailUrl(picture.getThumbnailUrl()).build();
    }
}
