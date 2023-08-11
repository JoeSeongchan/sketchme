package com.dutaduta.sketchme.videoconference.service;

import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.global.exception.ForbiddenException;
import com.dutaduta.sketchme.global.exception.InternalServerErrorException;
import com.dutaduta.sketchme.meeting.dao.MeetingRepository;
import com.dutaduta.sketchme.meeting.domain.Meeting;
import com.dutaduta.sketchme.meeting.domain.MeetingStatus;
import com.dutaduta.sketchme.oidc.dto.UserInfoInAccessTokenDTO;
//import com.dutaduta.sketchme.videoconference.controller.response.*;
import com.dutaduta.sketchme.videoconference.service.request.ReviewRegisterServiceRequest;
import com.dutaduta.sketchme.videoconference.service.response.ConnectionCreateResponse;
import com.dutaduta.sketchme.videoconference.service.response.SessionGetResponse;
import io.openvidu.java.client.Connection;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class VideoConferenceService {

    private final MeetingRepository meetingRepository;
    private final RandomSessionIdGenerator randomSessionIdGenerator;
    private final OpenViduAPIService openViduAPIService;
    private final TimelapseService timelapseService;


    public SessionGetResponse makeSession(UserInfoInAccessTokenDTO userInfo, long meetingId) {
        Meeting meeting = getApprovedOrRunningMeeting(userInfo, meetingId);

        String sessionId = meeting.getVideoConferenceRoomSessionId();
        // 화상 방이 열려 있으면, 해당 세션 값을 가져와서 리턴
        if(isMeetingRunning(meeting, sessionId)){
            log.info("세션이 열려있습니다. 세션 값은 {}입니다.",sessionId);
            return SessionGetResponse.builder().sessionId(sessionId).build();
        }

        // 아직 세션이 발급되지 않았으면 세션 값을 만들어서 OpenVidu에 등록 후 DB에 저장
        sessionId = randomSessionIdGenerator.generate();

        for(int i=1;i<=6;i++){
            try{
                openViduAPIService.createSession(sessionId);
                meeting.setVideoConferenceRoomSessionId(sessionId);
                meeting.setMeetingStatus(MeetingStatus.RUNNING);
                if(i>=5){
                    throw  new InternalServerErrorException("랜덤한 세션 ID 생성에 실패했습니다.");
                }
                log.info("session 발급 후 meeting 상태: {}",meeting.getMeetingStatus().name());
                break;
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        return SessionGetResponse.builder().sessionId(sessionId).build();
    }

    private static boolean isMeetingRunning(Meeting meeting, String sessionId) {
        return meeting.getMeetingStatus().equals(MeetingStatus.RUNNING);
    }

    private Meeting getApprovedOrRunningMeeting(UserInfoInAccessTokenDTO userInfo, long meetingId) {
        // meetingID로 meeting 조회
        Optional<Meeting> optionalMeeting = meetingRepository.findById(meetingId);
        if(optionalMeeting.isEmpty()){
            throw new BadRequestException("존재하지 않는 미팅입니다.");
        }
        Meeting meeting = optionalMeeting.get();
        if(!meeting.getMeetingStatus().equals(MeetingStatus.APPROVED) &&
                !(meeting.getMeetingStatus().equals(MeetingStatus.RUNNING))){
            throw new BadRequestException("\"수락 중\" 상태가 아닌 미팅입니다.");
        }
        if(userInfo.getUserId()!=meeting.getUser().getId()
        && userInfo.getArtistId()!=meeting.getArtist().getId()){
            throw new ForbiddenException("참여할 권한이 없는 미팅입니다.");
        }
        if(meeting.getStartDateTime().isAfter(LocalDateTime.now())){
            throw new BadRequestException("아직 참여 시간이 되지 않은 미팅입니다.");
        }
        return meeting;
    }

    public ConnectionCreateResponse createConnection(long meetingId, UserInfoInAccessTokenDTO userInfo) {
        // meeting 을 가져온다.
        Meeting meeting = getApprovedOrRunningMeeting(userInfo, meetingId);

        String sessionId = meeting.getVideoConferenceRoomSessionId();
        // 화상 방이 열려 있지 않은 경우 (세션이 발급되지 않은 경우) 예외 발생
        if(!isMeetingRunning(meeting, sessionId)){
            throw new BadRequestException("화상 방이 아직 열려 있지 않습니다.");
        }

        // 세션을 가져와서 OpenVidu API 서버에게 Connection 발급을 요청한다.
        Connection connection = openViduAPIService.createConnection(sessionId);

        // 가져온 Connection 안에 담겨 있는 ConnectionCreateResponse에 담아서 전달한다.
        return ConnectionCreateResponse.builder().token(connection.getToken()).build();
    }

    public void closeRoom(long meetingId, UserInfoInAccessTokenDTO userInfo) {
        // meeting 을 가져온다.
        Meeting meeting = getApprovedOrRunningMeeting(userInfo, meetingId);

        String sessionId = meeting.getVideoConferenceRoomSessionId();
        // 화상 방이 열려 있지 않은 경우 (세션이 발급되지 않은 경우) 예외 발생
        if(!isMeetingRunning(meeting, sessionId)){
            throw new BadRequestException("화상 방이 아직 열려 있지 않습니다.");
        }

        openViduAPIService.deleteSession(sessionId);
    }
}
