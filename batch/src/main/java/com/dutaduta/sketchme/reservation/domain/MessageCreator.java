package com.dutaduta.sketchme.reservation.domain;

public class MessageCreator {

    public static String createContent(Meeting meeting, String memberType) {
        if(MemberType.BOT_LIVE_STARTED.name().equals(memberType)) {
            return "미팅이 시작되었습니다! 접속해주세요.";
        }
        return String.format("{\"meetingID\": %d,\"startDateTime\": \"%s\"}",
                meeting.getId(),meeting.getStartDateTime());
    }
}
