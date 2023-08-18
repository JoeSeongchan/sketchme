package com.dutaduta.sketchme.videoconference.dao;

import com.dutaduta.sketchme.IntegrationTestSupport;
import com.dutaduta.sketchme.meeting.dao.MeetingRepository;
import com.dutaduta.sketchme.meeting.domain.Meeting;
import com.dutaduta.sketchme.member.dao.ArtistRepository;
import com.dutaduta.sketchme.member.dao.UserRepository;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class VideoConferenceRepositoryTest extends IntegrationTestSupport {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;



    @Test
    @DisplayName("Id 값으로 Meeting을 조회한다.")
    void findById() {
        // given
        User user1 = createUser("u_nick1");
        User user2 = createUser("u_nick2");
        Artist artist2 = createArtist("a_nick2",user2);
        List<User> userList = List.of(user1,user2);
        Meeting meeting = createMeeting(user1, artist2);
        userRepository.saveAll(userList);
        artistRepository.save(artist2);
        meetingRepository.save(meeting);

        // when
        Meeting output = meetingRepository.findById(meeting.getId()).get();


        // then
        assertThat(output).isNotNull();
        assertThat(output.getId()).isEqualTo(meeting.getId());
    }

    private static Meeting createMeeting(User user1, Artist artist2) {
        return Meeting.builder()
                .user(user1)
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