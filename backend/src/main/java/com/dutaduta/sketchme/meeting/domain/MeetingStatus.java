package com.dutaduta.sketchme.meeting.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;

@Getter
@RequiredArgsConstructor
public enum MeetingStatus {
    WAITING("대기 중 (고객으로부터 신청을 받고 아티스트의 승인을 기다리는 중)"),
    APPROVED("승인 (아티스트의 승인을 받고, 화상 미팅을 기다리는 중)"),
    DENIED("거부 (아티스트가 예약을 거부한 상태)"),
    COMPLETED("완료 (화상 미팅이 끝난 상태)"),
    CANCELLED("취소 (고객이나 아티스트가 예약을 취소한 경우"),
    RUNNING("화상 회의 진행 중"), WAITING_REVIEW("리뷰를 기다리는 중");

    private final String text;
}
