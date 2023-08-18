package com.dutaduta.sketchme.chat.dto;


import com.dutaduta.sketchme.chat.domain.ChatRoom;
import com.dutaduta.sketchme.member.constant.MemberType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BunchOfChatRoomResponseDTO {
    private Long chatRoomID;
    private Long userID;
    private Long userIDOfArtist;
    private String lastChat;
    private MemberType lastChatType;
    private LocalDateTime timeLastChatCreated;
    private String chatPartnerImageURL;
    private String chatPartnerName;

    public static BunchOfChatRoomResponseDTO toDTO(ChatRoom chatRoom, MemberType memberType) {
        if (MemberType.USER.equals(memberType)) {
            return BunchOfChatRoomResponseDTO.builder()
                    .userID(chatRoom.getUser().getId())
                    .userIDOfArtist(chatRoom.getArtist().getUser().getId())
                    .chatRoomID(chatRoom.getId())
                    .chatPartnerImageURL(chatRoom.getArtist().getProfileImgUrl())
                    .chatPartnerName(chatRoom.getArtist().getNickname())
                    .timeLastChatCreated(
                            chatRoom.getLastChat() == null ? null : chatRoom.getLastChat().getCreatedDateTime()
                    )
                    .lastChat(chatRoom.getLastChat() == null ? null : chatRoom.getLastChat().getContent())
                    .lastChatType(chatRoom.getLastChat().getMemberType())
                    .build();
        }
        return BunchOfChatRoomResponseDTO.builder()
                .userID(chatRoom.getUser().getId())
                .userIDOfArtist(chatRoom.getArtist().getUser().getId())
                .chatRoomID(chatRoom.getId())
                .chatPartnerImageURL(chatRoom.getUser().getProfileImgUrl())
                .chatPartnerName(chatRoom.getUser().getNickname())
                .timeLastChatCreated(
                        chatRoom.getLastChat() == null ? null : chatRoom.getLastChat().getCreatedDateTime()
                )
                .lastChat(chatRoom.getLastChat() == null ? null : chatRoom.getLastChat().getContent())
                .lastChatType(chatRoom.getLastChat().getMemberType())
                .build();
    }
}
