package com.dutaduta.sketchme.chat.dto;


import com.dutaduta.sketchme.chat.domain.ChatRoom;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateOrGetRoomResponseDTO {

    //아직 리턴값 결정안되서 우선 한번에 보내는걸로
    //정렬 기준이 필요함 -> 새로 만든 방은 채팅 안쳐도 우선 맨위로 가야함
    //그러면 채팅이 없는 경우에 방생성 시간으로 정렬하도록 해야될듯
    @NotNull
    private Long userID;
    @NotNull
    private Long userIDOfArtist;
    @NotNull
    private String content;
    @NotNull
    private LocalDateTime lastChatCreatedTime;
    @NotNull
    private String artistProfileImgUrl;
    @NotNull
    private String artistName;
    @NotNull
    private Long chatRoomID;

    public static CreateOrGetRoomResponseDTO toDTO(ChatRoom chatRoom) {
        return CreateOrGetRoomResponseDTO.builder()
                .chatRoomID(chatRoom.getId())
                .userIDOfArtist(chatRoom.getArtist().getUser().getId())
                .userID(chatRoom.getUser().getId())
                .artistProfileImgUrl(chatRoom.getArtist().getProfileImgUrl())
                .artistName(chatRoom.getArtist().getNickname())
                .content(chatRoom.getLastChat().getContent())
                .lastChatCreatedTime(chatRoom.getLastChat().getCreatedDateTime())
                .build();
    }
}
