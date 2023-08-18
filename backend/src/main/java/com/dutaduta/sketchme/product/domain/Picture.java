package com.dutaduta.sketchme.product.domain;

import com.dutaduta.sketchme.common.domain.BaseEntity;
import com.dutaduta.sketchme.common.domain.Category;
import com.dutaduta.sketchme.meeting.domain.Meeting;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "picture")
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Picture extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "is_open")
    private boolean isOpen;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Setter
    @Column(name = "url", length = 1024)
    private String url;
    @Setter
    @Column(name = "thumbnail_url", length = 1024)
    private String thumbnailUrl;
    @Column(name = "is_drawn_in_app")
    private boolean isDrawnInApp;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @OneToMany(mappedBy = "picture")
    private List<PictureHashtag> hashtagList;

    public void updateImgUrl(String url, String thumbnailUrl){
        this.url = url;
        this.thumbnailUrl = thumbnailUrl;
    }

    public void updateIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public void updateIsOpen(Boolean isOpen) {this.isOpen = isOpen; }

}
