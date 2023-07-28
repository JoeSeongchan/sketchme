package com.dutaduta.sketchme.common.domain;

import com.dutaduta.sketchme.member.domain.Artist;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "category")
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 512)
    private String name;

    @Column(length = 1024)
    private String description;

    private boolean isOpen;

    private boolean isDeleted;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    private Long approximatePrice;
}
