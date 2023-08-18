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
public class PictureResponse {

    @NotNull
    private Long id;

    @NotNull
    private ImgUrlResponse pictureImgUrl;

    private String title;

    @NotBlank
    private String writer;

    @NotNull
    private ImgUrlResponse writerImgUrl;

    private Long categoryID;

    private List<HashtagResponse> hashtags;

    private Long price;

    private String review;

    private String reviewWriter;

    private BigDecimal rating;

    // -----------------------------

    @NotNull
    private Boolean isOpen;

    @NotNull
    private boolean isDrawnInApp;

    private Long meetingID;

    @NotNull
    private Long userID; // 작가의 사용자 id

    @NotNull
    private Long artistID;

    private Long customerID; // 그림을 구매한 고객의 id

    public static PictureResponse of(Picture picture, ImgUrlResponse imgUrlResponse) {
        PictureResponseBuilder builder =  PictureResponse.builder()
                .pictureImgUrl(imgUrlResponse)
                .id(picture.getId())
                .isOpen(picture.isOpen())
                .isDrawnInApp(picture.isDrawnInApp())
                .artistID(picture.getArtist().getId())
                .userID(picture.getArtist().getUser().getId());

        if(picture.getCategory() != null) {
            builder.categoryID(picture.getCategory().getId())
                    .title(picture.getCategory().getName())
                    .price(picture.getCategory().getApproximatePrice());
        }

        if(picture.getMeeting() != null) {
            builder.meetingID(picture.getMeeting().getId());
        }

        if(picture.getUser() != null) {
            builder.customerID(picture.getUser().getId());
        }

        return builder.build();
    }

    public static PictureResponse of(Picture picture, ImgUrlResponse imgUrlResponse, List<HashtagResponse> hashtags, Review review) {
        PictureResponseBuilder builder =  PictureResponse.builder()
                .pictureImgUrl(imgUrlResponse)
                .id(picture.getId())
                .isOpen(picture.isOpen())
                .isDrawnInApp(picture.isDrawnInApp())
                .artistID(picture.getArtist().getId())
                .userID(picture.getArtist().getUser().getId())
                .writer(picture.getArtist().getNickname())
                .writerImgUrl(ImgUrlResponse.builder().imgUrl(picture.getArtist().getProfileImgUrl()).thumbnailUrl(picture.getArtist().getProfileThumbnailImgUrl()).build())
                .title(picture.getCategory().getName())
                .price(picture.getCategory().getApproximatePrice())
                .hashtags(hashtags);

        // 해당 그림이 속해있는 카테고리가 비공개일 경우 카테고리id로 -1 반환 (프론트에서 id 기준으로 라우팅 막을 수 있도록)
        if(picture.getCategory().isOpen()) {
            builder.categoryID(picture.getCategory().getId());
        }
        if(!picture.getCategory().isOpen()) {
            builder.categoryID(-1L);
        }

        if(picture.getMeeting() != null) {
            builder.meetingID(picture.getMeeting().getId());
        }

        if(picture.getUser() != null) {
            builder.customerID(picture.getUser().getId());
        }

        if(review != null) {
            builder.review(review.getContent())
                    .reviewWriter(review.getUser().getNickname())
                    .rating(review.getRating());
        }

        return builder.build();
    }
}
