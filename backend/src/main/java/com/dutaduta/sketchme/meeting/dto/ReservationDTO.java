package com.dutaduta.sketchme.meeting.dto;

import com.dutaduta.sketchme.global.exception.BadRequestException;
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
public class ReservationDTO {
    @PositiveOrZero
    private Long categoryID;

    @PositiveOrZero
    private Long userID;

    @NotNull
    @PositiveOrZero
    private Long artistID;

    @NotNull
    private LocalDateTime datetime;

    @NotBlank
    private String content;

    @NotNull
    private Boolean isOpen;
    
    public void setUserID(Long userID) {
        if(userID==null) throw new BadRequestException("정보가 잘못되었습니다");
        this.userID = userID;
    }
}
