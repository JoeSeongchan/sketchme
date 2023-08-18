package com.dutaduta.sketchme.chat.domain;

import com.dutaduta.sketchme.common.domain.BaseEntity;
import com.dutaduta.sketchme.member.constant.MemberType;
import com.dutaduta.sketchme.member.domain.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "chat")
@SuperBuilder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(length = 1024, nullable = false)
    private String content;

    @Enumerated(value = EnumType.STRING)
    private MemberType memberType;

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;

        if(chatRoom.getLastChat()!=this) {
            chatRoom.setLastChat(this);
        }
    }
}
