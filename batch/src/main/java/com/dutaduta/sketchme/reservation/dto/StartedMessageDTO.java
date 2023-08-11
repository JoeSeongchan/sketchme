package com.dutaduta.sketchme.reservation.dto;


import com.dutaduta.sketchme.reservation.domain.Meeting;
import com.dutaduta.sketchme.reservation.domain.MemberType;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@Log4j2
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StartedMessageDTO {
    private Long senderID;
    private Long receiverID;
    private LocalDateTime timestamp;
    private String content;
    private Long chatRoomID;
    private MemberType senderType; //token에 userType 담고나면 변경해야될 사항

    public static StartedMessageDTO of(Meeting meeting) {
        return StartedMessageDTO.builder()
                .chatRoomID(meeting.getChatRoom())
                .senderID(meeting.getArtist().getUser().getId())
                .receiverID(meeting.getUser())
                .content("미팅이 시작되었습니다! 접속해주세요.")//여기 메세지 집어놔야 함
                .senderType(MemberType.BOT_LIVE_STARTED)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
