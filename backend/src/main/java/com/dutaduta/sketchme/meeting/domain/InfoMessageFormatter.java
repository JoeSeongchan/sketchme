package com.dutaduta.sketchme.meeting.domain;

import com.dutaduta.sketchme.member.constant.MemberType;

public class InfoMessageFormatter {
    public static String create(Meeting savedMeeting, MemberType memberType) {
        if(MemberType.BOT_RESERVATION.equals(memberType) || MemberType.BOT_LIVE_INFO.equals(memberType)) {
            return String.format("{\"meetingID\" : %s}", savedMeeting.getId());
        }
        if(MemberType.BOT_LIVE_INFO.equals(memberType)) {
            String.format("{\"meetingID\": %d, \"startDateTime\": %s}",
                    savedMeeting.getId(), savedMeeting.getStartDateTime());
        }
        return null;
    }
}
