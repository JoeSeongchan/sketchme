package com.dutaduta.sketchme.product.dto;

import com.dutaduta.sketchme.common.dto.HashtagResponse;
import com.dutaduta.sketchme.file.dto.ImgUrlResponse;
import com.dutaduta.sketchme.product.domain.Picture;
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
public class PictureDeleteRequest {

    @NotNull(message = "카테고리 아이디가 필요합니다.")
    private Long categoryID;

    @NotNull(message = "그림 아이디가 필요합니다.")
    private Long pictureID;

}
