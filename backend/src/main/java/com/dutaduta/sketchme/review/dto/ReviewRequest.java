package com.dutaduta.sketchme.review.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@NoArgsConstructor
public class ReviewRequest {
    private Long meetingID;

    private Long reviewID;

    @NotNull(message = "별점을 0~5 사이로 입력해 주세요.")
    @Max(5) @Min(0)
    private BigDecimal rating;

    @NotNull(message = "리뷰 내용을 입력해 주세요.")
    private String content;
}
