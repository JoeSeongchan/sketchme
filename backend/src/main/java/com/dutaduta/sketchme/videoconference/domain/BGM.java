package com.dutaduta.sketchme.videoconference.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "bgm")
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BGM {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 1024)
    private String name;
    @Column(length = 1024)
    private String uri;
}
