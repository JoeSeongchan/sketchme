package com.dutaduta.sketchme.product.service;

import com.dutaduta.sketchme.IntegrationTestSupport;
import com.dutaduta.sketchme.common.Constant;
import com.dutaduta.sketchme.meeting.dao.MeetingRepository;
import com.dutaduta.sketchme.meeting.domain.Meeting;
import com.dutaduta.sketchme.meeting.domain.MeetingStatus;
import com.dutaduta.sketchme.member.dao.ArtistRepository;
import com.dutaduta.sketchme.member.dao.UserRepository;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import com.dutaduta.sketchme.oidc.dto.UserInfoInAccessTokenDTO;
import com.dutaduta.sketchme.product.dao.PictureRepository;
import com.dutaduta.sketchme.videoconference.service.OpenViduAPIService;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductServiceTest extends IntegrationTestSupport {
    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PictureRepository pictureRepository;

    @MockBean
    private OpenViduAPIService openViduAPIService;

    @Test
    @DisplayName("파일을 정상적으로 저장한다. (Happy Case)")
    public void saveFileInDirectoryHappyCase(){
        // given
        deleteFileServerDirectory();
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

        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);

        // when
        String newFileName = productService.saveLivePicture(userInfo, meeting.getId(),
                LocalDateTime.of(1900, 1, 1, 1, 1), multipartFile);
        // then
        System.out.println("newFileName = " + newFileName);
        assertThat(new File(Constant.LIVE_PICTURE_DIRECTORY+"/"+meeting.getId()+"/"+newFileName).exists()).isTrue();
    }

    private static void deleteFileServerDirectory() {
        try {
            FileUtils.deleteDirectory(new File(Constant.FILESERVER_DIRECTORY));
        } catch (IOException e) {
            System.out.println("fileserver 폴더 삭제 불가합니다.");
        }
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