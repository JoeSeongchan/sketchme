package com.dutaduta.sketchme.member.domain;

import com.dutaduta.sketchme.common.domain.BaseEntity;
import com.dutaduta.sketchme.global.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.log4j.Log4j2;

/**
 * 서비스 가입자
 */
@Entity
@Table(name = "users", uniqueConstraints= @UniqueConstraint(columnNames = {"oauthId", "oauthType"}))
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Log4j2
//@ToString
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1024)
    private String email;

    @Column(length = 128)
    private String nickname;

    @Column(length = 512)
    private String phoneNo;

    @Column(length = 1024)
    private String oauthId;

    @Enumerated(value = EnumType.STRING)
    private OAuthType oauthType;

    @Column(length = 1024)
    private String profileImgUrl;

    @Column(length = 1024)
    private String profileThumbnailImgUrl;

    @Column(length = 1024)
    private String description;

    private boolean isLogined;

    private boolean isDebuted;

    private boolean isOpen;

    private boolean isDeleted;

    // Artist와 양방향 일대일 설정
    @OneToOne
    @JoinColumn(name="artist_id")
    private Artist artist;

    // 연관관계 편의 메소드
    public void setArtist(Artist artist) {
        if(this.artist==artist) {return;}
        if(this.artist!=null && !this.artist.isDeactivated()) throw new BadRequestException("실패");
        this.artist = artist;
        artist.setUser(this);
    }

    public void updateIsDebuted(boolean isDebuted){
        this.isDebuted = isDebuted;
    }

    public void updateIsLogined(boolean isLogined) { this.isLogined = isLogined; }

    public void updateIsDeleted(boolean isDeleted) { this.isDeleted = isDeleted; }

    public void updateNickname(String nickname) { this.nickname = nickname; }

    public void updateImgUrl(String profileImgUrl, String profileThumbnailImgUrl) {
        this.profileImgUrl = profileImgUrl;
        this.profileThumbnailImgUrl = profileThumbnailImgUrl;
    }

}
