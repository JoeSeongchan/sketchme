package com.dutaduta.sketchme.meeting.domain;

import com.dutaduta.sketchme.chat.domain.ChatRoom;
import com.dutaduta.sketchme.common.domain.BaseEntity;
import com.dutaduta.sketchme.common.domain.Category;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Entity
@Table(name = "meeting")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "chatroom_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startDateTime;

    @Column(length = 1024)
    private String content;

    private boolean isOpen;

    @Enumerated(value = EnumType.STRING)
    private MeetingStatus meetingStatus;

    private Long exactPrice;

    @Enumerated(value = EnumType.STRING)
    private Payment payment;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;
}
