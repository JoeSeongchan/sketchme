package com.dutaduta.sketchme.meeting.domain;

import com.dutaduta.sketchme.chat.domain.ChatRoom;
import com.dutaduta.sketchme.common.domain.BaseEntity;
import com.dutaduta.sketchme.common.domain.Category;
import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.global.exception.ForbiddenException;
import com.dutaduta.sketchme.meeting.dto.ReservationDTO;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import com.dutaduta.sketchme.product.domain.Picture;
import com.dutaduta.sketchme.product.domain.Timelapse;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Table(name = "meeting")
@SuperBuilder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meeting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
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
    private LocalDateTime startDateTime;

    @Column(length = 1024)
    private String content;

    private boolean isOpen;

    @Setter
    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private MeetingStatus meetingStatus = MeetingStatus.WAITING;

    private Long exactPrice;

    @Enumerated(value = EnumType.STRING)
    private Payment payment;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    @Column(unique = true)
    private String videoConferenceRoomSessionId;

    @OneToOne(mappedBy = "meeting")
    private Timelapse timelapse;

    @OneToOne(mappedBy = "meeting")
    private Picture picture;

    @Setter
    // 고객 비디오 연결 ID
    private String userVideoConnectionId;

    @Setter
    // 아티스트 비디오 연결 ID
    private String artistVideoConnectionId;

    @Setter
    // 아티스트 캔버스 연결 ID
    private String artistCanvasConnectionId;

    // 무료인 경우 자동으로 가격 0으로 저장되도록 함
    @PrePersist
    public void setPriceZeroIfMeetingIsFree() {
        this.exactPrice = this.exactPrice == null ? 0L : this.exactPrice;
    }

    public static Meeting createMeeting(User user, Artist artist, Category category
            , ReservationDTO reservationDto, ChatRoom chatRoom) {
        if(LocalDateTime.now().isAfter(reservationDto.getDatetime())) {
            throw new BadRequestException("날짜를 정확히 작성해주세요");
        }

        return Meeting.builder()
                .user(user)
                .artist(artist)
                .category(category)
                .exactPrice(category.getApproximatePrice()) // 일단 카테고리 가격 그대로 반영하기로 함! (가격 조정 불가)
                .startDateTime(reservationDto.getDatetime())
                .content(reservationDto.getContent())
                .isOpen(reservationDto.getIsOpen())
                .chatRoom(chatRoom)
                .build();
    }

    public void checkInvalidDetermination(MeetingStatus meetingStatus) {
        if (MeetingStatus.WAITING.equals(meetingStatus)
                || MeetingStatus.COMPLETED.equals(meetingStatus)) {
            throw new ForbiddenException("잘못된 요청입니다");
        }
    }

    public void confirm(MeetingStatus meetingStatus) {
        if (!MeetingStatus.APPROVED.equals(meetingStatus)) return;

        if (!MeetingStatus.WAITING.equals(this.meetingStatus)) throw new ForbiddenException("잘못된 요청입니다");
        this.meetingStatus = MeetingStatus.APPROVED;
    }

    public void refuse(MeetingStatus meetingStatus) {
        if (!MeetingStatus.DENIED.equals(meetingStatus)) return;

        if (!MeetingStatus.WAITING.equals(this.meetingStatus)) throw new ForbiddenException("잘못된 요청입니다");
        this.meetingStatus = MeetingStatus.DENIED;
    }

    public void cancel(MeetingStatus meetingStatus) {
        if (!MeetingStatus.CANCELLED.equals(meetingStatus)) return;

        if (!MeetingStatus.APPROVED.equals(this.meetingStatus)) throw new ForbiddenException("잘못된 요청입니다");
        this.meetingStatus = MeetingStatus.CANCELLED;
    }

    public void setVideoConferenceRoomSessionId(String videoConferenceRoomSessionId) {
        this.videoConferenceRoomSessionId = videoConferenceRoomSessionId;
    }

    public void setMeetingStatus(MeetingStatus meetingStatus) {
        this.meetingStatus = meetingStatus;
    }

    public void setTimelapse(Timelapse timelapse){
        this.timelapse = timelapse;
    }

    public boolean isOwnedByUser(long userId) {
        return userId == this.user.getId();
    }
}
