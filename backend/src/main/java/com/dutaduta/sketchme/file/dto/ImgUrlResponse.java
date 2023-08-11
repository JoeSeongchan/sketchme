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
    private String profileImgUrl;

    @NotBlank
    private String profileThumbnailUrl;

    public static ImgUrlResponse of(UploadResponse uploadResponse) {
        return ImgUrlResponse.builder()
                .profileImgUrl(uploadResponse.getImageURL())
                .profileThumbnailUrl(uploadResponse.getThumbnailURL()).build();
    }

    public static ImgUrlResponse of(Picture picture) {
        return ImgUrlResponse.builder()
                .profileImgUrl(picture.getUrl())
                .profileThumbnailUrl(picture.getThumbnailUrl()).build();
    }
}
