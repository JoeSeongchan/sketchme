package com.dutaduta.sketchme.meeting.domain;

import com.dutaduta.sketchme.member.constant.MemberType;

public class InfoMessageFormatter {
    public static String create(Meeting savedMeeting, MemberType memberType) {
        if(MemberType.BOT_RESERVATION.equals(memberType)) {
            return String.format("{\"meetingID\" : %s}", savedMeeting.getId());
        }
        return null;
    }
}
