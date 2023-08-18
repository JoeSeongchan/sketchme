package com.dutaduta.sketchme.member.domain;

import com.dutaduta.sketchme.common.domain.BaseEntity;
import com.dutaduta.sketchme.global.exception.BadRequestException;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Table(name = "artist")
@SuperBuilder
@Getter
//@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Artist extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512)
    private String nickname;

    @Column(length = 1024)
    private String profileImgUrl;

    @Column(length = 1024)
    private String profileThumbnailImgUrl;

//    @Temporal(value = TemporalType.TIMESTAMP)
//    private Date debutDateTime;

    @Column(length = 1024)
    private String description;

    private boolean isOpen;

    private boolean isDeactivated;

    @Setter
    @OneToOne(mappedBy = "artist")
    private User user;

    @OneToMany(mappedBy = "artist")
    private List<ArtistHashtag> artistHashtagList;

    @OneToMany(mappedBy = "artist")
    private List<FavoriteArtist> favoriteArtistList;

    public void setUser(User user) {
        if(this.user==user) return;
        if(this.user!=null) throw new BadRequestException("실패");
        this.user = user;
    }

    public void updateIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void deactivate() {
        this.isDeactivated = true;
    }

    public void updateImgUrl(String profileImgUrl, String profileThumbnailImgUrl) {
        this.profileImgUrl = profileImgUrl;
        this.profileThumbnailImgUrl = profileThumbnailImgUrl;
    }
    public void updateDescription(String description){
        this.description = description;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void reactivate() {this.isDeactivated = false; }
}
