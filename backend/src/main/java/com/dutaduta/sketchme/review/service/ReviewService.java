package com.dutaduta.sketchme.review.service;

import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.global.exception.ForbiddenException;
import com.dutaduta.sketchme.meeting.dao.MeetingRepository;
import com.dutaduta.sketchme.meeting.domain.Meeting;
import com.dutaduta.sketchme.meeting.domain.MeetingStatus;
import com.dutaduta.sketchme.oidc.dto.UserInfoInAccessTokenDTO;
import com.dutaduta.sketchme.review.dao.ReviewRepository;
import com.dutaduta.sketchme.review.domain.Review;
import com.dutaduta.sketchme.review.dto.ReviewRequest;
import com.dutaduta.sketchme.review.service.request.ReviewCreateServiceRequest;

import java.util.Objects;

import com.dutaduta.sketchme.videoconference.service.request.ReviewRegisterServiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ReviewService {

    private final MeetingRepository meetingRepository;
    private final ReviewRepository reviewRepository;

    public long registerReview(UserInfoInAccessTokenDTO userInfo, long meetingId, ReviewCreateServiceRequest reviewCreateServiceRequest) {
        // 해당 유저가 미팅의 고객인지 확인한다.
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new BadRequestException("존재하지 않는 미팅입니다."));
        if (meeting.getUser().getId() != userInfo.getUserId()) {
            throw new BadRequestException("리뷰를 남길 수 없는 사용자입니다.");
        }

        // 상대방 유저가 미팅의 아티스트인지 확인한다.
        if (meeting.getArtist().getId() != reviewCreateServiceRequest.getArtistId()) {
            throw new BadRequestException("이 Artist는 미팅에 참여하고 있지 않습니다. 리뷰를 남길 수 없습니다.");
        }

        // 미팅이 끝난 상태인지 확인한다. (세션이 닫히면서 미팅이 종료됨을 DB에 기록하고 나서 Review를 등록한다.)
        if (!meeting.getMeetingStatus().equals(MeetingStatus.WAITING_REVIEW)) {
            throw new BadRequestException("아직 리뷰를 남길 수 없습니다. 미팅이 끝나야 리뷰를 남길 수 있습니다.");
        }

        // 리뷰 정보를 등록한다.
        Review review = reviewCreateServiceRequest.toEntity(userInfo.getUserId());
        reviewRepository.save(review);
        meeting.setMeetingStatus(MeetingStatus.COMPLETED);
        // 등록한 리뷰의 ID 값을 리턴한다.
        return review.getId();
    }

    public void insertReview(ReviewRequest reviewRequest, Long userId) {
        Meeting meeting = meetingRepository.findById(reviewRequest.getMeetingID()).orElseThrow(()->new BadRequestException("미팅이 존재하지 않습니다."));

        // 해당 미팅에 이미 리뷰가 작성되어 있다면 쓸 수 없도록 (하나의 미팅에 하나의 리뷰만 가능)
        if (reviewRepository.existsByMeetingId(reviewRequest.getMeetingID())) {
            throw new BadRequestException("리뷰가 이미 작성되어 있습니다.");
        }

        // meeting의 userId와 현재 요청을 보낸 userId가 같지 않으면 리뷰 쓸 수 없도록
        if (Objects.equals(meeting.getUser().getId(), userId)) {
            Review review = Review.createReview(meeting, reviewRequest);
            reviewRepository.save(review);
        } else {
            throw new ForbiddenException("리뷰를 쓸 권한이 없습니다.");
        }
    }


    public void modifyReview(ReviewRequest reviewRequest, Long userId) {
        // 본인이 아니면 리뷰 수정할 수 없도록
        Review review = reviewRepository.findById(reviewRequest.getReviewID()).orElseThrow(()->new ForbiddenException("리뷰가 존재하지 않습니다."));
        if (Objects.equals(review.getUser().getId(), userId)) {
            review.updateReview(reviewRequest);
        } else {
            throw new ForbiddenException("리뷰를 수정할 권한이 없습니다.");
        }
    }

    public void deleteReview(Long reviewID, Long userId) {
        // 본인이 아니면 리뷰 삭제할 수 없도록
        Review review = reviewRepository.findById(reviewID).orElseThrow(()->new BadRequestException("리뷰가 존재하지 않습니다."));
        if (Objects.equals(review.getUser().getId(), userId)) {
            review.deleteReview();
        } else {
            throw new ForbiddenException("리뷰를 삭제할 권한이 없습니다.");
        }
    }

    public void registerReview(ReviewRegisterServiceRequest requestDTO) {

    }
}
