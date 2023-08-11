package com.dutaduta.sketchme.meeting.dto;

import com.dutaduta.sketchme.meeting.domain.Meeting;
import com.dutaduta.sketchme.meeting.domain.MeetingStatus;
import com.dutaduta.sketchme.meeting.domain.Payment;
import com.dutaduta.sketchme.meeting.domain.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class MeetingInfoDTO {
    @NotNull(message = "meeting ID는 공백이면 안 됩니다.")
    private Long meetingID;

    @NotBlank(message = "카테고리 이름은 공백이어선 안 됩니다.")
    private String categoryName;

    @NotBlank(message = "유저 닉네임은 공백이어선 안 됩니다.")
    private String userNickname;

    @NotBlank(message = "아티스트 닉네임은 공백이어선 안 됩니다.")
    private String artistNickname;

    @PositiveOrZero(message = "유저 ID는 음수여선 안 됩니다.")
    private Long userID;

    @PositiveOrZero(message = "아티스트 ID는 음수여선 안 됩니다.")
    private Long artistID;

    private String userEmail;

    private String artistEmail;

    private String content;

    @NotNull
    private LocalDateTime startDatetime; // 예약 일시

    @NotNull
    private LocalDateTime createDatetime; // 신청 일시

    @NotNull
    private Long exactPrice;

    @NotNull
    private MeetingStatus meetingStatus;

    private Payment payment;

    private PaymentStatus paymentStatus;

    @NotNull
    private Boolean isOpen;

    public static MeetingInfoDTO of(Meeting meeting){
        return MeetingInfoDTO.builder()
                .meetingID(meeting.getId())
                .categoryName(meeting.getCategory().getName())
                .userNickname(meeting.getUser().getNickname())  // user, artist 정보가 다 필요함...
                .artistNickname(meeting.getArtist().getNickname())
                .userID(meeting.getUser().getId())
                .artistID(meeting.getArtist().getId())
                .userEmail(meeting.getUser().getEmail())
                .artistEmail(meeting.getArtist().getUser().getEmail())
                .content(meeting.getContent())
                .startDatetime(meeting.getStartDateTime())
                .createDatetime(meeting.getCreatedDateTime())
                .exactPrice(meeting.getExactPrice())
                .meetingStatus(meeting.getMeetingStatus())
                .payment(meeting.getPayment())
                .paymentStatus(meeting.getPaymentStatus())
                .isOpen(meeting.isOpen())
                .build();
    }
}
