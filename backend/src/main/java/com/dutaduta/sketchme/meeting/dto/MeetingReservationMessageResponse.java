package com.dutaduta.sketchme.meeting.dto;

import com.dutaduta.sketchme.member.constant.MemberType;
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
public class MeetingReservationMessageResponse {

    @PositiveOrZero
    private Long messageID;
    @PositiveOrZero
    private Long meetingID;
    @PositiveOrZero
    private Long senderID;
    @PositiveOrZero
    private Long receiverID;
    @Setter
    private LocalDateTime timestamp;
    @NotBlank
    private String content;
    @PositiveOrZero
    private Long chatRoomID;
    @NotNull
    private MemberType senderType; //token에 userType 담고나면 변경해야될 사항
}
