package com.dutaduta.sketchme.reservation.domain;

import com.dutaduta.sketchme.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "meeting")
@SuperBuilder
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chatroom_id")
    private Long chatRoom;

    @Column(name = "user_id")
    private Long user;

    @OneToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    private LocalDateTime startDateTime;

    @Column(length = 1024)
    private String content;

    private boolean isOpen;

    @Enumerated(value = EnumType.STRING)
    @Builder.Default // 초기값 대기중으로 설정
    private MeetingStatus meetingStatus = MeetingStatus.WAITING;

    private Long exactPrice;

    @Enumerated(value = EnumType.STRING)
    private Payment payment;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;


    @PrePersist
    public void prePersist() {
        this.exactPrice = this.exactPrice == null ? 0L : this.exactPrice;
    }
}
