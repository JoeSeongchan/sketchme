package com.dutaduta.sketchme.videoconference.domain;

import com.dutaduta.sketchme.common.domain.BaseEntity;
import com.dutaduta.sketchme.meeting.domain.Meeting;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "videoconference")
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VideoConference extends BaseEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "url", length = 1024)
    private String url;
    @OneToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;
}
