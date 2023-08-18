package com.dutaduta.sketchme.meeting.dto;


import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.meeting.domain.MeetingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.apache.kafka.common.annotation.InterfaceStability;

@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeterminateMeetingRequest {

    @NotNull(message = "meetingID 정보가 필요합니다")
    private Long meetingID;
    
    private Long artistID;

    @NotNull(message = "status가 잘못되었습니다")
    private MeetingStatus statusDetermination;

    public void setArtistID(Long artistID) {
        if(artistID==null) throw new BadRequestException("정보가 잘못되었습니다");
        this.artistID = artistID;
    }
}
