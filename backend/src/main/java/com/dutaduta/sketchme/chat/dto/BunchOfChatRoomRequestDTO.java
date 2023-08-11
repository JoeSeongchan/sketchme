package com.dutaduta.sketchme.chat.dto;


import com.dutaduta.sketchme.member.constant.MemberType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BunchOfChatRoomRequestDTO {
    @NotNull
    private Long userID;
    @NotNull
    private MemberType memberType;
}
