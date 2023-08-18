package com.dutaduta.sketchme.chat.dto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CreateOrGetRoomRequestDTO {
    private Long requestUserID;

    @NotNull
    private Long userIDOfArtist;

    public void setRequestUserID(Long requestUserID) {
        this.requestUserID = requestUserID;
    }
}
