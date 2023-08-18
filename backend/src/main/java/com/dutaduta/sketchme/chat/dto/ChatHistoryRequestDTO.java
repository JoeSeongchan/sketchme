package com.dutaduta.sketchme.chat.dto;


import com.dutaduta.sketchme.member.constant.MemberType;
import lombok.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatHistoryRequestDTO {
    private Long roomID;
    private int pageNum;
    private MemberType memberType;
}
