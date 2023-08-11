package com.dutaduta.sketchme.videoconference.service;

import com.dutaduta.sketchme.IntegrationTestSupport;
import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.meeting.dao.MeetingRepository;
import com.dutaduta.sketchme.meeting.domain.Meeting;
import com.dutaduta.sketchme.meeting.domain.MeetingStatus;
import com.dutaduta.sketchme.member.dao.ArtistRepository;
import com.dutaduta.sketchme.member.dao.UserRepository;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import com.dutaduta.sketchme.oidc.dto.UserInfoInAccessTokenDTO;
import com.dutaduta.sketchme.videoconference.service.response.ConnectionCreateResponse;
import com.dutaduta.sketchme.videoconference.service.response.SessionGetResponse;
import io.openvidu.java.client.Connection;
import io.openvidu.java.client.OpenViduHttpException;
import io.openvidu.java.client.OpenViduJavaClientException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;



class VideoConferenceServiceTest extends IntegrationTestSupport {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @MockBean
    private OpenViduAPIService openViduAPIService;

    @Autowired
    private VideoConferenceService videoConferenceService;

    @MockBean
    private RandomSessionIdGenerator sessionIdGenerator;



    @Test
    @DisplayName("세션 ID가 DB에 존재하지 않으면, 세션 ID를 생성하여 OpenVidu API에 등록하며 DB에 저장한다. (Happy Case)")
    void createSessionWhenSessionIsNotExisted() throws OpenViduJavaClientException, OpenViduHttpException {
        // given
        User user1 = createUser("u_nick1");
        User user2 = createUser("u_nick2");
        Artist artist2 = createArtist("a_nick2",user2);
        List<User> userList = List.of(user1,user2);
        LocalDateTime startDateTime = LocalDateTime.of(1900,1,1,1,1,1);
        Meeting meeting = createMeeting(user1, artist2, startDateTime);
        meeting.setMeetingStatus(MeetingStatus.APPROVED);
        userRepository.saveAll(userList);
        artistRepository.save(artist2);
        meetingRepository.save(meeting);
        UserInfoInAccessTokenDTO userInfo = createUserInfoInAccessToken(user1);
        when(openViduAPIService.createSession(Mockito.anyString())).thenReturn("random-session-id");
        when(sessionIdGenerator.generate()).thenReturn("random-session-id");

        // when
        SessionGetResponse response = videoConferenceService.makeSession(userInfo,meeting.getId());


        // then
        assertThat(response.getSessionId()).isEqualTo("random-session-id");
    }

    @Test
    @DisplayName("미팅이 존재하지 않으면 세션 요청을 거부하고 BadRequestException을 방출한다.")
    void createSessionWithNotExistedMeeting() {
        // given
        User user1 = createUser("u_nick1");
        User user2 = createUser("u_nick2");
        Artist artist2 = createArtist("a_nick2",user2);
        List<User> userList = List.of(user1,user2);

        userRepository.saveAll(userList);
        artistRepository.save(artist2);
        UserInfoInAccessTokenDTO userInfo = createUserInfoInAccessToken(user1);


        // when
        assertThatThrownBy(()->videoConferenceService.makeSession(userInfo, -1)).isInstanceOf(BadRequestException.class).hasMessage("존재하지 않는 미팅입니다.");

        // then
    }

    @Test
    @DisplayName("현재 미팅 상태가 APPROVED 또는 RUNNING이 아닌 경우, 세션 요청을 거부하고 BadRequestException을 방출한다.")
    void createSessionWithWrongMeetingStatus() {
        // given
        User user1 = createUser("u_nick1");
        User user2 = createUser("u_nick2");
        Artist artist2 = createArtist("a_nick2",user2);
        List<User> userList = List.of(user1,user2);
        LocalDateTime startDateTime = LocalDateTime.of(1900,1,1,1,1,1);
        Meeting meeting = createMeeting(user1, artist2, startDateTime);
        meeting.setMeetingStatus(MeetingStatus.WAITING);
        meeting.setVideoConferenceRoomSessionId("blank");
        userRepository.saveAll(userList);
        artistRepository.save(artist2);
        meetingRepository.save(meeting);
        UserInfoInAccessTokenDTO userInfo = createUserInfoInAccessToken(user1);


        // when
        assertThatThrownBy(()->videoConferenceService.makeSession(userInfo, meeting.getId())).isInstanceOf(BadRequestException.class).hasMessage("\"수락 중\" 상태가 아닌 미팅입니다.");

        // then

    }

    @Test
    @DisplayName("DB에 세션이 이미 존재하는 경우, 해당 세션 ID를 DB에서 가져와서 사용자에게 돌려준다.")
    void returnSessionWhenSessionIsExisted() throws OpenViduJavaClientException, OpenViduHttpException {
        // given
        User user1 = createUser("u_nick1");
        User user2 = createUser("u_nick2");
        Artist artist2 = createArtist("a_nick2",user2);
        List<User> userList = List.of(user1,user2);
        LocalDateTime startDateTime = LocalDateTime.of(1900,1,1,1,1,1);
        Meeting meeting = createMeeting(user1, artist2, startDateTime);
        meeting.setMeetingStatus(MeetingStatus.RUNNING);
        meeting.setVideoConferenceRoomSessionId("random-session-id");
        userRepository.saveAll(userList);
        artistRepository.save(artist2);
        meetingRepository.save(meeting);
        UserInfoInAccessTokenDTO userInfo = createUserInfoInAccessToken(user1);


        // when
        SessionGetResponse response = videoConferenceService.makeSession(userInfo,meeting.getId());


        // then
        assertThat(response.getSessionId()).isEqualTo("random-session-id");

    }

    @Test
    @DisplayName("세션이 유효한 경우, 연결을 생성해서, 그 안에 담긴 토큰을 사용자에게 반환한다.")
    void createConnectionWithValidSessionId() throws OpenViduJavaClientException, OpenViduHttpException {
        // given
        // 사용자, 아티스트, 미팅을 만든다.
        User user1 = createUser("u_nick1");
        User user2 = createUser("u_nick2");
        Artist artist2 = createArtist("a_nick2",user2);
        List<User> userList = List.of(user1,user2);
        LocalDateTime startDateTime = LocalDateTime.of(1900,1,1,1,1,1);
        Meeting meeting = createMeeting(user1, artist2, startDateTime);
        meeting.setMeetingStatus(MeetingStatus.RUNNING);
        meeting.setVideoConferenceRoomSessionId("random-session-id");
        userRepository.saveAll(userList);
        artistRepository.save(artist2);
        meetingRepository.save(meeting);
        UserInfoInAccessTokenDTO userInfo = createUserInfoInAccessToken(user1);

        // 세션을 만든다.
        String sessionId="random-session-id";
        when(sessionIdGenerator.generate()).thenReturn(sessionId);
        when(openViduAPIService.createSession(Mockito.anyString())).thenReturn(sessionId);

        videoConferenceService.makeSession(userInfo, meeting.getId());


        Connection connection = mock(Connection.class);
        // 연결을 만들기 위해 Mock 설정한다.
        when(connection.getToken()).thenReturn("token");
        when(openViduAPIService.createConnection(Mockito.anyString())).thenReturn(connection);

        // when
        // 연결을 만든다.
        ConnectionCreateResponse response = videoConferenceService.createConnection(meeting.getId(), userInfo);


        // then
        // 검증한다.
        assertThat(response).extracting("token").isEqualTo("token");
    }

    @Test
    @DisplayName("세션이 유효하지 않은 경우, 예외를 방출한다.")
    void createConnectionWithInvalidSessionId() throws OpenViduJavaClientException, OpenViduHttpException {
        // given
        // 사용자, 아티스트, 미팅을 만든다.
        User user1 = createUser("u_nick1");
        User user2 = createUser("u_nick2");
        Artist artist2 = createArtist("a_nick2",user2);
        List<User> userList = List.of(user1,user2);
        LocalDateTime startDateTime = LocalDateTime.of(1900,1,1,1,1,1);
        Meeting meeting = createMeeting(user1, artist2, startDateTime);
        meeting.setMeetingStatus(MeetingStatus.RUNNING);
        meeting.setVideoConferenceRoomSessionId("random-session-id");
        userRepository.saveAll(userList);
        artistRepository.save(artist2);
        meetingRepository.save(meeting);
        UserInfoInAccessTokenDTO userInfo = createUserInfoInAccessToken(user1);

        // 세션을 만든다.
        String sessionId="random-session-id";
        when(sessionIdGenerator.generate()).thenReturn(sessionId);
        when(openViduAPIService.createSession(Mockito.anyString())).thenReturn(sessionId);

        videoConferenceService.makeSession(userInfo, meeting.getId());

        // 현재 미팅 상태를 COMPLETED로 설정한다.
        meeting.setMeetingStatus(MeetingStatus.COMPLETED);

        // when
        assertThatThrownBy(()->videoConferenceService.createConnection(meeting.getId(), userInfo)).isInstanceOf(BadRequestException.class);


        // then
    }

    @Test
    @DisplayName("")
    void createSessionWithDuplicateSessionId() throws OpenViduJavaClientException, OpenViduHttpException {
        // given
        // 사용자, 아티스트, 미팅을 만든다.
        User user1 = createUser("u_nick1");
        User user2 = createUser("u_nick2");
        User user3 = createUser("u_nick3");

        Artist artist1 = createArtist("a_nick1",user1);
        Artist artist2 = createArtist("a_nick2",user2);

        List<User> userList = List.of(user1,user2,user3);
        List<Artist> artistList = List.of(artist1,artist2);

        final String RANDOM_SESSION_ID = "random-session-id";

        // user1 <-> artist2 미팅
        LocalDateTime startDateTime1 = LocalDateTime.of(1900,1,1,1,1,1);
        Meeting meeting1 = createMeeting(user1, artist2, startDateTime1);
        meeting1.setMeetingStatus(MeetingStatus.RUNNING);
        meeting1.setVideoConferenceRoomSessionId(RANDOM_SESSION_ID);

        // user2 <-> artist1 미팅
        LocalDateTime startDateTime2 = LocalDateTime.of(1900,1,1,1,1,1);
        Meeting meeting2 = createMeeting(user2, artist1, startDateTime2);
        meeting2.setMeetingStatus(MeetingStatus.RUNNING);
        meeting2.setVideoConferenceRoomSessionId(RANDOM_SESSION_ID);
        List<Meeting> meetingList = List.of(meeting1, meeting2);

        userRepository.saveAll(userList);
        artistRepository.saveAll(artistList);

        // 동일한 세션 ID를 다른 미팅이 사용하고 있다면 예외를 방출한다.
        assertThatThrownBy(()->{meetingRepository.saveAll(meetingList);}).isInstanceOf(Exception.class);
    }

    private static UserInfoInAccessTokenDTO createUserInfoInAccessToken(User user1) {
        return UserInfoInAccessTokenDTO.builder().userId(user1.getId()).build();
    }


    private static Meeting createMeeting(User user1, Artist artist2, LocalDateTime startDateTime) {
        return Meeting.builder()
                .user(user1)
                .startDateTime(startDateTime)
                .artist(artist2)
                .build();
    }

    private static Artist createArtist(String nickname, User user1) {
        return Artist.builder()
                .nickname(nickname)
                .user(user1).build();
    }

    private static User createUser(String nickname) {
        return User.builder()
                .nickname(nickname)
                .build();
    }

}