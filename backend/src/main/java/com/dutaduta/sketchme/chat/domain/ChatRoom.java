package com.dutaduta.sketchme.chat.domain;

import com.dutaduta.sketchme.common.domain.BaseEntity;
import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.meeting.domain.Meeting;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import jakarta.persistence.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Set;

@Entity
@Log4j2
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chatroom", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "artist_id"}))
public class ChatRoom extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @OneToOne
    @JoinColumn(name = "last_chat_id")
    private Chat lastChat;

    @OneToMany(mappedBy = "chatRoom")
    private List<Meeting> meeting;

    public void setLastChat(Chat lastChat) {
        this.lastChat = lastChat;

        if (lastChat.getChatRoom() != this) {
            lastChat.setChatRoom(this);
        }
    }

    public static ChatRoom createRoom(User requestUser, Artist artist) {
        Validator validator;
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
        ChatRoom chatRoom = ChatRoom.builder()
                .user(requestUser)
                .artist(artist)
                .build();

        Set<ConstraintViolation<ChatRoom>> violation = validator.validate(chatRoom);
        if (!violation.isEmpty()) {
            throw new BadRequestException("방을 생성할 수 없습니다.");
        }
        return chatRoom;
    }
}
