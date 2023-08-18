package com.dutaduta.sketchme.review.dto;

import com.dutaduta.sketchme.file.dto.ImgUrlResponse;
import com.dutaduta.sketchme.product.domain.Picture;
import com.dutaduta.sketchme.product.domain.Timelapse;
import com.dutaduta.sketchme.review.domain.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class ReviewDetailResponse {

    private ImgUrlResponse pictureImgUrl;

    private ImgUrlResponse timelapseImgUrl;

    private String content;

    private String category; // 카테고리 이름

    private String reviewer; // 리뷰 작성자

    private LocalDateTime registeredDatetime;

    private BigDecimal rating;

    public static ReviewDetailResponse of(Review review, Picture picture, Timelapse timelapse) {
        return ReviewDetailResponse.builder()
                .pictureImgUrl(ImgUrlResponse.builder().imgUrl(picture.getUrl()).thumbnailUrl(picture.getThumbnailUrl()).build())
                .timelapseImgUrl(ImgUrlResponse.builder().imgUrl(timelapse.getUrl()).imgUrl(timelapse.getThumbnailUrl()).build())
                .content(review.getContent())
                .reviewer(review.getUser().getNickname())
                .category(review.getMeeting().getCategory().getName())
                .registeredDatetime(review.getCreatedDateTime())
                .rating(review.getRating()).build();
    }

    public static ReviewDetailResponse of(Review review, Picture picture) {
        return ReviewDetailResponse.builder()
                .pictureImgUrl(ImgUrlResponse.builder().imgUrl(picture.getUrl()).thumbnailUrl(picture.getThumbnailUrl()).build())
                .content(review.getContent())
                .reviewer(review.getUser().getNickname())
                .category(review.getMeeting().getCategory().getName())
                .registeredDatetime(review.getCreatedDateTime())
                .rating(review.getRating()).build();
    }
}
