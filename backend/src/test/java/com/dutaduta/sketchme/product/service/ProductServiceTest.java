package com.dutaduta.sketchme.product.service;

import com.dutaduta.sketchme.IntegrationTestSupport;
import com.dutaduta.sketchme.common.Constant;
import com.dutaduta.sketchme.file.service.FileService;
import com.dutaduta.sketchme.meeting.dao.MeetingRepository;
import com.dutaduta.sketchme.meeting.domain.Meeting;
import com.dutaduta.sketchme.meeting.domain.MeetingStatus;
import com.dutaduta.sketchme.member.dao.ArtistRepository;
import com.dutaduta.sketchme.member.dao.UserRepository;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import com.dutaduta.sketchme.oidc.dto.UserInfoInAccessTokenDTO;
import com.dutaduta.sketchme.product.dao.PictureRepository;
import com.dutaduta.sketchme.product.dao.TimelapseRepository;
import com.dutaduta.sketchme.product.domain.Timelapse;
import com.dutaduta.sketchme.product.service.response.TimelapseGetResponse;
import com.dutaduta.sketchme.videoconference.service.OpenViduAPIService;
import org.apache.commons.io.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
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

    @MockBean
    private FileService fileService;

    @Autowired
    private TimelapseRepository timelapseRepository;

    @Test
    @DisplayName("미팅의 실시간 그림을 저장한다. (Happy Case)")
    void saveLivePictureHappyCase() throws IOException {
        // given
        User user1 = createUser("u_nick1");
        User user2 = createUser("u_nick2");
        Artist artist1 = createArtist("a_nick1", user1);
        Artist artist2 = createArtist("a_nick2", user1);
        Meeting meeting1 = createMeeting(user1, artist2, LocalDateTime.now());
        meeting1.setMeetingStatus(MeetingStatus.COMPLETED);
        UserInfoInAccessTokenDTO userInfo = createUserInfoInAccessToken(user2,artist2);
        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        Mockito.when(multipartFile.getContentType()).thenReturn("image/png");

        // when
        productService.saveLivePicture(userInfo, meeting1.getId(), LocalDateTime.now(), multipartFile);

        // then
    }

    @Test
    @DisplayName("미팅의 타임랩스 파일을 가져온다. ")
    void getTimelapseHappyCase() throws IOException {
        // given
        User user1 = createUser("u_nick1");
        User user2 = createUser("u_nick2");
        Artist artist1 = createArtist("a_nick1", user1);
        Artist artist2 = createArtist("a_nick2", user1);
        Meeting meeting1 = createMeeting(user1, artist2, LocalDateTime.now());
        meeting1.setMeetingStatus(MeetingStatus.COMPLETED);
        UserInfoInAccessTokenDTO userInfo = createUserInfoInAccessToken(user2,artist2);
        Mockito.when(fileService.getDir(Mockito.anyString())).thenReturn(new File(String.format("%s/%d",Constant.TIMELAPSE_DIRECTORY,meeting1.getId())));
        Mockito.when(fileService.getFile(Mockito.anyString(),Mockito.any())).thenReturn(new File(String.format("%s/%d/o_timelapse.png",Constant.TIMELAPSE_DIRECTORY,meeting1.getId())));
        Timelapse timelapse = createTimelapse(meeting1);

        // when
        TimelapseGetResponse response = productService.getTimelapse(userInfo, meeting1.getId());

        // then
        Assertions.assertThat(response.getTimelapseUrl()).isEqualTo(".\\fileserver\\TIMELAPSE\\1\\o_timelapse.png");
    }

    private Timelapse createTimelapse(Meeting meeting) {
        Timelapse timelapse = Timelapse.builder().isOpen(true).build();
        timelapse.setMeeting(meeting);
        timelapseRepository.save(timelapse);
        meeting.setTimelapse(timelapse);
        return timelapse;
    }

    @Test
    @DisplayName("")
    void saveFinalPictureHappyCase() {
        // given

        // when

        // then
    }



    private static void deleteFileServerDirectory() {
        try {
            FileUtils.deleteDirectory(new File(Constant.FILESERVER_DIRECTORY));
        } catch (IOException e) {
            System.out.println("fileserver 폴더 삭제 불가합니다.");
        }
    }

    private static UserInfoInAccessTokenDTO createUserInfoInAccessToken(User user,Artist artist) {
        return UserInfoInAccessTokenDTO.builder().userId(user.getId()).artistId(artist.getId()).build();
    }


    private Meeting createMeeting(User user, Artist artist, LocalDateTime startDateTime) {
        Meeting meeting =  Meeting.builder()
                .user(user)
                .startDateTime(startDateTime)
                .artist(artist)
                .build();
        meetingRepository.save(meeting);
        return meeting;
    }

    private Artist createArtist(String nickname, User user1) {
        Artist artist =  Artist.builder()
                .nickname(nickname)
                .user(user1).build();
        artistRepository.save(artist);
        return artist;
    }

    private User createUser(String nickname) {
        User user =  User.builder()
                .nickname(nickname)
                .build();
        userRepository.save(user);
        return user;
    }
}