package com.dutaduta.sketchme.review.service.request;

import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.meeting.domain.Meeting;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import com.dutaduta.sketchme.review.domain.Review;
import java.math.BigDecimal;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewCreateServiceRequest {
	private double rating;
	private String content;
	private long userId;
	private long artistId;
	private long meetingId;

	public Review toEntity(long userId){
		if(rating<0 || rating>5){
			throw new BadRequestException("별점은 0에서 5 사이 값이어야 한다.");
		}
		return Review.builder()
			.rating(new BigDecimal(String.format("%2.1f",rating)))
			.content(content)
			.user(User.builder().id(userId).build())
			.artist(Artist.builder().id(artistId).build())
			.meeting(Meeting.builder().id(meetingId).build()).build();
	}

}
