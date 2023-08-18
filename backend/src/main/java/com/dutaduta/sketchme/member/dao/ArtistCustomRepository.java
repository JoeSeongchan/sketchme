package com.dutaduta.sketchme.member.dao;

import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.OAuthType;
import com.dutaduta.sketchme.member.domain.QArtist;
import com.dutaduta.sketchme.member.domain.QUser;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Repository
public class ArtistCustomRepository {

    private final JPAQueryFactory queryFactory;

    public ArtistCustomRepository(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public Artist findArtistByUserIdentifier(String userOauthID, OAuthType userOAuthType) {
        QArtist artist = QArtist.artist;
        QUser user = QUser.user;
        return queryFactory.selectFrom(artist)
                .where(artist.user.eq(
                        JPAExpressions.select(user)
                                .where(user.oauthId.eq(userOauthID).and(user.oauthType.eq(userOAuthType)))
                ))
                .fetchOne();
    }

    public Artist findArtistByUserIdAndNotDeactivated(Long artistIDOfUser) {
        QArtist artist = QArtist.artist;
        Artist result = queryFactory.selectFrom(artist)
                .where(artist.user.id.eq(artistIDOfUser)
                        .and(artist.isDeactivated.eq(false))).fetchOne();
        if(result ==null) throw new BadRequestException("잘못된 요청입니다");
        return result;
    }
}
