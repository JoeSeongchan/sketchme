package com.dutaduta.sketchme.meeting.dto;


import com.dutaduta.sketchme.meeting.domain.MeetingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeterminateMeetingRequest {

    @NotNull(message = "meetingID 정보가 필요합니다")
    private Long meetingID;
    @NotNull(message = "아티스트 정보가 없습니다")
    private Long artistID;
    @NotNull(message = "아티스트 정보가 없습니다")
    private MeetingStatus statusDetermination;
}
