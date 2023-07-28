package com.dutaduta.sketchme.product.domain;

import com.dutaduta.sketchme.common.domain.Hashtag;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PictureHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "picture_id")
    Picture picture;

    @ManyToOne
    @JoinColumn(name = "hashtag_id")
    Hashtag hashtag;
}
