package com.dutaduta.sketchme.reservation.domain;

import com.dutaduta.sketchme.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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

    @OneToOne(mappedBy = "artist")
    private User user;
}