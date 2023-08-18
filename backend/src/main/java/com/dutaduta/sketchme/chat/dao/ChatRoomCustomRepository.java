package com.dutaduta.sketchme.chat.dao;

import com.dutaduta.sketchme.chat.domain.ChatRoom;
import com.dutaduta.sketchme.chat.domain.QChatRoom;
import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.global.exception.ForbiddenException;
import com.dutaduta.sketchme.member.constant.MemberType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Repository
public class ChatRoomCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ChatRoomCustomRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public List<ChatRoom> findBunchOfChatRoomByUser(Long userID, MemberType memberType) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        return queryFactory.selectFrom(chatRoom)
                .where(createUserCondition(userID, memberType, chatRoom))
                .fetch();
    }

    public ChatRoom findChatRoomByUserAndUserTypeAndRoomNumber(Long roomID, Long userID, MemberType memberType) {
        QChatRoom chatRoom = QChatRoom.chatRoom;
        ChatRoom result = queryFactory.selectFrom(chatRoom)
                .where(createUserCondition(userID, memberType, chatRoom).and(chatRoom.id.eq(roomID)))
                .fetchOne();
        if(result==null) throw new ForbiddenException("해당 방이 없거나 참여할 권한이 없습니다.");
        return result;
    }

    private BooleanBuilder createUserCondition(Long userID, MemberType memberType, QChatRoom chatRoom) {
        BooleanBuilder builder = new BooleanBuilder();

        if (MemberType.USER.equals(memberType) || MemberType.BOT_RESERVATION.equals(memberType)) {
            builder.and(chatRoom.user.id.eq(userID)
                    .and(chatRoom.user.isDeleted.eq(false))
                    .and(chatRoom.artist.isDeactivated.eq(false)));
            return builder;
        }

        if (MemberType.ARTIST.equals(memberType) ||
                MemberType.BOT_LIVE_INFO.equals(memberType) ||
                MemberType.BOT_LIVE_STARTED.equals(memberType) ||
                MemberType.BOT_RESERVATION_CONFIRM.equals(memberType) ||
                MemberType.BOT_RESERVATION_CANCEL.equals(memberType)) {
            builder.and(chatRoom.artist.user.id.eq(userID)
                    .and(chatRoom.user.isDeleted.eq(false))
                    .and(chatRoom.artist.isDeactivated.eq(false)));
            return builder;
        }
        throw new BadRequestException("잘못된 요청입니다");
    }
}
