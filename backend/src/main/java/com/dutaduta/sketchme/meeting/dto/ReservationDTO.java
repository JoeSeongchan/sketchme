package com.dutaduta.sketchme.meeting.dto;

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

    @PositiveOrZero
    private Long artistID;

    @NotNull
    private LocalDateTime datetime;

    @NotBlank
    private String content;

    @NotNull
    private Boolean isOpen;
}
