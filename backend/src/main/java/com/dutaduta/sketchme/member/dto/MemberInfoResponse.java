package com.dutaduta.sketchme.member.dto;

import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class MemberInfoResponse {

    private Long memberID;

    private String memberStatus; // 현재 작가인지, 사용자인지

    private String email;

    private String nickname;

    private String profileImgUrl;

    private String description;

    private boolean isDebuted;

    private boolean isOpen;

    private boolean isLogined;

    private LocalDateTime CreatedDateTime;

    private LocalDateTime UpdatedDateTime;

    /**
     * repository를 통해 조회한 user entity를 dto로 변환하는 메서드
     * @param user
     */
    public MemberInfoResponse(User user, Long id) {
        this.memberID = id;
        this.memberStatus = "user";
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImgUrl = user.getProfileImgUrl();
        this.description = user.getDescription();
        this.isDebuted = user.isDebuted();
        this.isOpen = user.isOpen();
        this.isLogined = user.isLogined();
        this.CreatedDateTime = user.getCreatedDateTime();
        this.UpdatedDateTime = user.getUpdatedDateTime();
    }

    public MemberInfoResponse(Artist artist, Long id) {
        this.memberID = id;
        this.memberStatus = "artist";
        this.email = artist.getUser().getEmail(); // email은 작가와 사용자 계정이 동일.
        this.nickname = artist.getNickname();
        this.profileImgUrl = artist.getProfileImgUrl();
        this.description = artist.getDescription();
        this.isDebuted = artist.getUser().isDebuted(); // 작가 등록 여부도 사용자 계정과 동일.
        this.isOpen = artist.isOpen();
        this.isLogined = artist.getUser().isLogined(); // 로그인 여부도 작가와 사용자 계정이 동일.
        this.CreatedDateTime = artist.getCreatedDateTime();
        this.UpdatedDateTime = artist.getUpdatedDateTime();
    }
}
