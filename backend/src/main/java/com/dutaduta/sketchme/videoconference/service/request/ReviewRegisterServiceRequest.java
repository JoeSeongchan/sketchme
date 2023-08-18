package com.dutaduta.sketchme.videoconference.service.request;

import lombok.*;

@NoArgsConstructor
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewRegisterServiceRequest {
    private double rating;
    private String review;
}
