package com.dutaduta.sketchme.videoconference.service.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class ReservationInfoGetResponse {
    private String artistNickname;
    private String customerNickname;
    private String email;
    private LocalDateTime reservationDateTime;
    private LocalDateTime applicationDateTime; // 신청일자
    private long paymentAmount; // 결제 금액
}
