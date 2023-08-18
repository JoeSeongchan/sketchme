package com.dutaduta.sketchme.review.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dutaduta.sketchme.IntegrationTestSupport;
import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.meeting.dao.MeetingRepository;
import com.dutaduta.sketchme.meeting.domain.Meeting;
import com.dutaduta.sketchme.meeting.domain.MeetingStatus;
import com.dutaduta.sketchme.member.dao.ArtistRepository;
import com.dutaduta.sketchme.member.dao.UserRepository;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import com.dutaduta.sketchme.oidc.dto.UserInfoInAccessTokenDTO;
import com.dutaduta.sketchme.review.controller.request.ReviewCreateRequest;
import com.dutaduta.sketchme.review.dao.ReviewRepository;
import com.dutaduta.sketchme.review.domain.Review;
import com.dutaduta.sketchme.review.service.request.ReviewCreateServiceRequest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ReviewServiceTest extends IntegrationTestSupport {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ArtistRepository artistRepository;

	@Autowired
	private MeetingRepository meetingRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private ReviewService reviewService;

	@Test
	@DisplayName("미팅이 끝난 다음에, 리뷰를 등록한다. (Happy Case)")
	public void registerReviewAfterMeeting(){
	 // given
		User user1 = createUser("u_nick1");
		User user2 = createUser("u_nick2");
		Artist artist2 = createArtist("a_nick2",user2);
		List<User> userList = List.of(user1,user2);
		LocalDateTime startDateTime = LocalDateTime.of(1900,1,1,1,1,1);
		Meeting meeting = createMeeting(user1, artist2, startDateTime);
		meeting.setMeetingStatus(MeetingStatus.WAITING_REVIEW);
		meeting.setVideoConferenceRoomSessionId("random-session-id");
		userRepository.saveAll(userList);
		artistRepository.save(artist2);
		meetingRepository.save(meeting);
		UserInfoInAccessTokenDTO userInfo = createUserInfoInAccessToken(user1);

		ReviewCreateServiceRequest request = ReviewCreateRequest.builder()
			.content("content1")
			.rating(3.6)
			.artistId(artist2.getId())
			.meetingId(meeting.getId())
			.build().toServiceRequest();
	 // when
		long reviewId = reviewService.registerReview(userInfo,meeting.getId(),request);

	 // then
		System.out.println("reviewId = " + reviewId);
		assertThat(reviewId).isGreaterThanOrEqualTo(0);
	}

	@Test
	@DisplayName("미팅이 끝나기 전에 리뷰를 등록하면 예외가 발생한다.")
	public void registerReviewNotAfterMeeting(){
		User user1 = createUser("u_nick1");
		User user2 = createUser("u_nick2");
		Artist artist2 = createArtist("a_nick2",user2);
		List<User> userList = List.of(user1,user2);
		LocalDateTime startDateTime = LocalDateTime.of(1900,1,1,1,1,1);
		Meeting meeting = createMeeting(user1, artist2, startDateTime);
		meeting.setMeetingStatus(MeetingStatus.RUNNING);
		meeting.setVideoConferenceRoomSessionId("random-session-id");
		userRepository.saveAll(userList);
		artistRepository.save(artist2);
		meetingRepository.save(meeting);
		UserInfoInAccessTokenDTO userInfo = createUserInfoInAccessToken(user1);

		ReviewCreateServiceRequest request = ReviewCreateRequest.builder()
			.content("content1")
			.rating(3.6)
			.artistId(artist2.getId())
			.meetingId(meeting.getId())
			.build().toServiceRequest();
		// when
		assertThatThrownBy(()->reviewService.registerReview(userInfo,meeting.getId(),request)).isInstanceOf(
			BadRequestException.class);

		// then
	}

	@Test
	@DisplayName("미팅의 고객이 아닌 사람이 리뷰를 등록하려고 할 때 예외가 발생한다.")
	public void registerReviewByAnonymous(){
		User user1 = createUser("u_nick1");
		User anonymous = createUser("u_anonymous");
		User user2 = createUser("u_nick2");
		Artist artist2 = createArtist("a_nick2",user2);
		List<User> userList = List.of(user1,user2,anonymous);
		LocalDateTime startDateTime = LocalDateTime.of(1900,1,1,1,1,1);
		Meeting meeting = createMeeting(user1, artist2, startDateTime);
		meeting.setMeetingStatus(MeetingStatus.WAITING_REVIEW);
		meeting.setVideoConferenceRoomSessionId("random-session-id");
		userRepository.saveAll(userList);
		artistRepository.save(artist2);
		meetingRepository.save(meeting);
		UserInfoInAccessTokenDTO userInfo = createUserInfoInAccessToken(anonymous);

		ReviewCreateServiceRequest request = ReviewCreateRequest.builder()
			.content("content1")
			.rating(3.6)
			.artistId(artist2.getId())
			.meetingId(meeting.getId())
			.build().toServiceRequest();
		// when
		assertThatThrownBy(()->reviewService.registerReview(userInfo,meeting.getId(),request)).isInstanceOf(
			BadRequestException.class);

		// then
	}

	@Test
	@DisplayName("미팅에 참여하고 있지 않은 아티스트에게 리뷰를 남길 때 예외가 발생한다.")
	public void registerReviewToNotProperArtist(){
	 // given
		User user1 = createUser("u_nick1");
		User user2 = createUser("u_nick2");
		User user3 = createUser("u_nick3");
		Artist artist2 = createArtist("a_nick2",user2);
		Artist otherArtist = createArtist("a_othernick",user3);
		List<Artist> artistList = List.of(artist2,otherArtist);
		List<User> userList = List.of(user1,user2,user3);

		LocalDateTime startDateTime = LocalDateTime.of(1900,1,1,1,1,1);
		Meeting meeting = createMeeting(user1, artist2, startDateTime);
		meeting.setMeetingStatus(MeetingStatus.WAITING_REVIEW);
		meeting.setVideoConferenceRoomSessionId("random-session-id");
		userRepository.saveAll(userList);
		artistRepository.saveAll(artistList);
		meetingRepository.save(meeting);
		UserInfoInAccessTokenDTO userInfo = createUserInfoInAccessToken(user1);

		ReviewCreateServiceRequest request = ReviewCreateRequest.builder()
			.content("content1")
			.rating(3.6)
			.artistId(otherArtist.getId())
			.meetingId(meeting.getId())
			.build().toServiceRequest();
	 // when
		assertThatThrownBy(()->reviewService.registerReview(userInfo,meeting.getId(),request)).isInstanceOf(
			BadRequestException.class);

	 // then
	}

	@Test
	@DisplayName("이미 리뷰를 등록했는데 중복해서 리뷰를 등록하려고 할 때 예외가 발생한다.")
	public void registerReviewDuplicatly(){
		// given
		User user1 = createUser("u_nick1");
		User user2 = createUser("u_nick2");
		Artist artist2 = createArtist("a_nick2",user2);
		List<User> userList = List.of(user1,user2);
		LocalDateTime startDateTime = LocalDateTime.of(1900,1,1,1,1,1);
		Meeting meeting = createMeeting(user1, artist2, startDateTime);
		meeting.setMeetingStatus(MeetingStatus.WAITING_REVIEW);
		meeting.setVideoConferenceRoomSessionId("random-session-id");
		userRepository.saveAll(userList);
		artistRepository.save(artist2);
		meetingRepository.save(meeting);
		UserInfoInAccessTokenDTO userInfo = createUserInfoInAccessToken(user1);

		ReviewCreateServiceRequest request = ReviewCreateRequest.builder()
			.content("content1")
			.rating(3.6)
			.artistId(artist2.getId())
			.meetingId(meeting.getId())
			.build().toServiceRequest();
		reviewService.registerReview(userInfo,meeting.getId(),request);

		// when
		assertThatThrownBy(()->reviewService.registerReview(userInfo,meeting.getId(),request)).isInstanceOf(BadRequestException.class);
	 // then
	}

	private static UserInfoInAccessTokenDTO createUserInfoInAccessToken(User user1) {
		return UserInfoInAccessTokenDTO.builder().userId(user1.getId()).build();
	}


	private static Meeting createMeeting(User user1, Artist artist2, LocalDateTime startDateTime) {
		return Meeting.builder()
			.user(user1)
			.startDateTime(startDateTime)
			.artist(artist2)
			.build();
	}

	private static Artist createArtist(String nickname, User user1) {
		return Artist.builder()
			.nickname(nickname)
			.user(user1).build();
	}

	private static User createUser(String nickname) {
		return User.builder()
			.nickname(nickname)
			.build();
	}

}