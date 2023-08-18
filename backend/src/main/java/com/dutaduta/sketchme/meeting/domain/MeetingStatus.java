package com.dutaduta.sketchme.meeting.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;

@Getter
@RequiredArgsConstructor
public enum MeetingStatus {
    WAITING("대기 중 (고객으로부터 신청을 받고 아티스트의 승인을 기다리는 중)"),
    APPROVED("예약이 승인되었습니다."),
    DENIED("예약이 거절되었습니다"),
    COMPLETED("화상 미팅이 완료되었습니다"),
    CANCELLED("예약이 취소되었습니다"),
    RUNNING("화상 회의 진행 중"), WAITING_REVIEW("리뷰를 기다리는 중");

    private final String text;
}
