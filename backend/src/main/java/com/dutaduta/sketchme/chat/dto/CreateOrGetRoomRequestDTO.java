package com.dutaduta.sketchme.chat.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CreateOrGetRoomRequestDTO {
    @NotNull
    private Long requestUserID;
    @NotNull
    private Long userIDOfArtist;
}
