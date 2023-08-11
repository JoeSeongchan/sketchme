package com.dutaduta.sketchme.chat.dao;

import com.dutaduta.sketchme.chat.domain.Chat;

import com.dutaduta.sketchme.chat.domain.ChatRoom;
import com.dutaduta.sketchme.chat.domain.QChat;
import com.dutaduta.sketchme.chat.domain.QChatRoom;
import com.dutaduta.sketchme.member.constant.MemberType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Transactional(readOnly = true)
@Repository
public class ChatCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ChatCustomRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Chat findLatestChatInChatRoom(Long chatRoomId) {
        QChat chat = QChat.chat;
        return queryFactory.select(chat)
                .where(chat.chatRoom.id.eq(chatRoomId))
                .from(chat)
                .orderBy(chat.id.desc())
                .limit(1)
                .fetchOne();
    }
//    public void save(MessageDTO messageDTO) {
//        QChat chat = QChat.chat;
//        QUser user = QUser.user;
//        QArtist artist = QArtist.artist;
//        QChatRoom chatRoom = QChatRoom.chatRoom;
//        queryFactory.insert(chat)
//                .columns(chat.chatRoom, chat.content,
//                        chat.sender, chat.receiver, chat.memberType)
//                .values(JPAExpressions.selectFrom(chatRoom)
//                                .where(chatRoom.id.eq(messageDTO.getChatRoomID())).fetchOne(),
//                                        messageDTO.getContent(),
//                                        selectSenderByMemberType(messageDTO, user, artist).fetchOne(),
//                                        selectReciverByMemberType(messageDTO,user, artist).fetchOne(),
//                                        messageDTO.getSenderType()
//                ).execute();
//    }
//
//    JPQLQuery<? extends BaseEntity> selectSenderByMemberType(MessageDTO messageDTO, QUser user, QArtist artist) {
//        if(messageDTO.getSenderType().equals(MemberType.USER)) {
//            return JPAExpressions.selectFrom(user).where(user.id.eq(messageDTO.getSenderID()));
//        }
//        return JPAExpressions.selectFrom(artist).where(artist.id.eq(messageDTO.getSenderID()));
//    }
//
//    JPQLQuery<? extends BaseEntity> selectReciverByMemberType(MessageDTO messageDTO, QUser user, QArtist artist) {
//        if(messageDTO.getSenderType().equals(MemberType.USER)) {
//            return JPAExpressions.selectFrom(user).where(user.id.eq(messageDTO.getSenderID()));
//        }
//        return JPAExpressions.selectFrom(artist).where(artist.id.eq(messageDTO.getSenderID()));
//    }
}
