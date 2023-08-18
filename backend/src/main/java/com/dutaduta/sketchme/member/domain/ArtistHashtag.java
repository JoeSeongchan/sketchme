package com.dutaduta.sketchme.member.domain;

import com.dutaduta.sketchme.common.domain.Hashtag;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ArtistHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    public static ArtistHashtag of(Artist artist, Hashtag hashtag) {
        return ArtistHashtag.builder().artist(artist).hashtag(hashtag).build();
    }
}
