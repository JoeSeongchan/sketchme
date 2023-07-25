package com.dutaduta.sketchme.member.domain;

import com.dutaduta.sketchme.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 서비스 가입자
 */
@Entity
@Table(name = "user")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1024)
    private String email;

    @Column(length = 512)
    private String phoneNo;

    @Column(length = 1024)
    private String oauthId;

    @Enumerated(value = EnumType.STRING)
    private OAuthType oAuthType;

    @Column(length = 1024)
    private String profileImgUrl;

    @Column(length = 1024)
    private String description;

    private boolean isLogined;

    private boolean isDebuted;

    private boolean isOpen;
}
