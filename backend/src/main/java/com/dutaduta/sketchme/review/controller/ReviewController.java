package com.dutaduta.sketchme.review.controller;

import com.dutaduta.sketchme.global.ResponseFormat;
import com.dutaduta.sketchme.oidc.dto.UserInfoInAccessTokenDTO;
import com.dutaduta.sketchme.oidc.jwt.JwtProvider;
import com.dutaduta.sketchme.oidc.jwt.JwtUtil;
import com.dutaduta.sketchme.review.controller.request.ReviewCreateRequest;
import com.dutaduta.sketchme.review.dto.ReviewDetailResponse;
import com.dutaduta.sketchme.review.dto.ReviewRequest;
import com.dutaduta.sketchme.review.service.ReviewService;
import com.dutaduta.sketchme.videoconference.controller.request.ReviewRegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
@CrossOrigin
public class ReviewController {

	private final ReviewService reviewService;
    private final JwtUtil jwtUtil;

	@PostMapping("api/meeting/{meetingId}/review")
	public ResponseEntity<ResponseFormat<Long>> registerReview(@Valid @RequestBody ReviewCreateRequest request, HttpServletRequest httpServletRequest, @PathVariable("meetingId") long meetingId){
		UserInfoInAccessTokenDTO userInfo = jwtUtil.extractUserInfo(httpServletRequest);
		long reviewId = reviewService.registerReview(userInfo, meetingId, request.toServiceRequest());
		return ResponseFormat.success(reviewId).toEntity();
	}

    @PostMapping("meeting/{meetingId}/rating-and-review")
    public ResponseEntity<ResponseFormat<Object>> registerRatingAndReview(@RequestBody ReviewRegisterRequest requestDTO, HttpServletRequest request){
        UserInfoInAccessTokenDTO userInfo = jwtUtil.extractUserInfo(request);
        reviewService.registerReview(requestDTO.toServiceRequest());
        return ResponseFormat.success().toEntity();
    }

    @PostMapping("/review")
    public ResponseEntity<ResponseFormat<String>> writeReview(@RequestBody @Valid ReviewRequest reviewRequest, HttpServletRequest request){
        log.info(reviewRequest.toString());
        Long userId = JwtProvider.getUserId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        reviewService.insertReview(reviewRequest, userId);
        return ResponseFormat.success("리뷰가 입력되었습니다.").toEntity();
    }

    @PutMapping("/review")
    public ResponseEntity<ResponseFormat<String>> modifyReview(@RequestBody @Valid ReviewRequest reviewRequest, HttpServletRequest request){
        Long userId = JwtProvider.getUserId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        reviewService.modifyReview(reviewRequest, userId);
        return ResponseFormat.success("리뷰 수정이 완료되었습니다.").toEntity();
    }

    @DeleteMapping("/review")
    public ResponseEntity<ResponseFormat<String>> deleteReview(@RequestBody Map<String, Long> reviewMap, HttpServletRequest request) {
        Long userId = JwtProvider.getUserId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        reviewService.deleteReview(reviewMap.get("reviewID"), userId);
        return ResponseFormat.success("리뷰가 삭제되었습니다.").toEntity();
    }

    @GetMapping("/review/detail/{id}")
    public ResponseEntity<ResponseFormat<ReviewDetailResponse>> getReviewDetail(@PathVariable Long id) {
        log.info("리뷰 상세보기");
        log.info("pictureID : " + id);
        ReviewDetailResponse reviewDetailResponse = reviewService.getReviewDetail(id);
        return ResponseFormat.success(reviewDetailResponse).toEntity();
    }

}
