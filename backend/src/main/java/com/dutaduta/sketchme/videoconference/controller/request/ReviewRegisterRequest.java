package com.dutaduta.sketchme.videoconference.controller.request;

import com.dutaduta.sketchme.videoconference.service.request.ReviewRegisterServiceRequest;
import lombok.*;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewRegisterRequest {
    private double rating;
    private String review;

    public ReviewRegisterServiceRequest toServiceRequest(){
        return ReviewRegisterServiceRequest.builder()
                .rating(rating)
                .review(review)
                .build();
    }
}
