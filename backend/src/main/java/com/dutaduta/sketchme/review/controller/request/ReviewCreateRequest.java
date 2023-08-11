package com.dutaduta.sketchme.review.controller.request;

import com.dutaduta.sketchme.review.service.request.ReviewCreateServiceRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewCreateRequest {
	@Positive
	private double rating;
	@NotBlank
	private String content;
	@Positive
	private long artistId;
	@Positive
	private long meetingId;

	public ReviewCreateServiceRequest toServiceRequest(){
		return ReviewCreateServiceRequest.builder()
			.rating(rating)
			.content(content)
			.artistId(artistId)
			.meetingId(meetingId)
			.build();
	}

}
