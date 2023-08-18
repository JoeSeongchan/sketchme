package com.dutaduta.sketchme.product.domain;

import com.dutaduta.sketchme.common.domain.BaseEntity;
import com.dutaduta.sketchme.meeting.domain.Meeting;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "timelapse")
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Timelapse extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url", length = 1024)
    private String url;
    @Column(name = "thumbnail_url", length = 1024)
    private String thumbnailUrl;
    @Setter
    @OneToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;
    private boolean isOpen;
    private boolean isDeleted;
}
