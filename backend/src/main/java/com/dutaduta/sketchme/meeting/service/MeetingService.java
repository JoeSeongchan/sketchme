package com.dutaduta.sketchme.meeting.service;

import com.dutaduta.sketchme.chat.constant.KafkaConstants;
import com.dutaduta.sketchme.chat.domain.ChatRoom;
import com.dutaduta.sketchme.chat.dto.CreateOrGetRoomRequestDTO;
import com.dutaduta.sketchme.chat.dto.MessageDTO;
import com.dutaduta.sketchme.chat.service.ChatRoomService;
import com.dutaduta.sketchme.common.dao.CategoryRepository;
import com.dutaduta.sketchme.common.domain.Category;
import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.meeting.dao.MeetingRepository;
import com.dutaduta.sketchme.meeting.domain.InfoMessageFormatter;
import com.dutaduta.sketchme.meeting.domain.Meeting;
import com.dutaduta.sketchme.meeting.domain.MeetingStatus;
import com.dutaduta.sketchme.meeting.dto.DeterminateMeetingRequest;
import com.dutaduta.sketchme.meeting.dto.MeetingInfoDTO;
import com.dutaduta.sketchme.meeting.dto.ReservationDTO;
import com.dutaduta.sketchme.member.constant.MemberType;
import com.dutaduta.sketchme.member.dao.ArtistRepository;
import com.dutaduta.sketchme.member.dao.UserRepository;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Log4j2
@Transactional
@AllArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    private final UserRepository userRepository;

    private final ArtistRepository artistRepository;
    private final CategoryRepository categoryRepository;
    private final ChatRoomService chatRoomService;
    private final KafkaTemplate<String, MessageDTO> kafkaTemplate;

    @Transactional
    public Long createMeeting(ReservationDTO reservationDto) {
        User user = userRepository.findByIdAndIsDeleted(reservationDto.getUserID(), false)
                .orElseThrow(() -> new BadRequestException("잘못된 요청입니다."));
        Artist artist = artistRepository.findByIdAndIsDeactivated(reservationDto.getArtistID(), false)
                .orElseThrow(() -> new BadRequestException("잘못된 요청입니다."));
        Category category = categoryRepository.getReferenceById(reservationDto.getCategoryID());
        CreateOrGetRoomRequestDTO createOrGetRoomRequestDTO = CreateOrGetRoomRequestDTO.builder()
                .requestUserID(user.getId())
                .userIDOfArtist(artist.getUser().getId())
                .build();
        //채팅방 가져오기
        ChatRoom chatRoom = chatRoomService.createRoomOrGetExistedRoom(createOrGetRoomRequestDTO);
        Meeting meeting = Meeting.createMeeting(user, artist, category, reservationDto, chatRoom);
        Meeting savedMeeting = meetingRepository.save(meeting);
        String content = InfoMessageFormatter.create(savedMeeting, MemberType.BOT_RESERVATION);
        MessageDTO messageDTO = MessageDTO.of(meeting, content);
        kafkaTemplate.send(KafkaConstants.KAFKA_MEETING, messageDTO.getSenderID().toString(), messageDTO);
        return savedMeeting.getId();
    }

    @Transactional
    public MeetingInfoDTO getMeetingInformation(Long id) {
        Meeting meeting = meetingRepository.findById(id).orElseThrow(() -> new BadRequestException("존재하지 않는 미팅입니다."));
        return MeetingInfoDTO.of(meeting);
    }

    public void determinate(DeterminateMeetingRequest meetingRequest) {
        Meeting meeting = meetingRepository.findByIdAndArtist_Id(
                meetingRequest.getMeetingID(), meetingRequest.getArtistID());
        meeting.checkInvalidDetermination(meetingRequest.getStatusDetermination());
        meeting.confirm(meetingRequest.getStatusDetermination());
        meeting.refuse(meetingRequest.getStatusDetermination());
        meeting.cancel(meetingRequest.getStatusDetermination());
        MessageDTO messageDTO = MessageDTO.of(meeting);
        kafkaTemplate.send(KafkaConstants.KAFKA_MEETING, messageDTO.getSenderID().toString(), messageDTO);

        //테스트용 api
        if(MeetingStatus.APPROVED.equals(meeting.getMeetingStatus())) {
            MessageDTO messageDTO1 = MessageDTO.builder()
                    .senderID(meeting.getUser().getId())
                    .receiverID(meeting.getArtist().getUser().getId())
                    .content(InfoMessageFormatter.create(meeting, MemberType.BOT_LIVE_INFO))
                    .timestamp(LocalDateTime.now())
                    .senderType(MemberType.BOT_LIVE_INFO)
                    .chatRoomID(meeting.getChatRoom().getId())
                    .build();
            kafkaTemplate.send(KafkaConstants.KAFKA_MEETING, messageDTO.getSenderID().toString(), messageDTO1);
        }
    }

    public Map<String, List<MeetingInfoDTO>> getMyMeetingList(Long userId, Long artistId) {
        Map<String, List<MeetingInfoDTO>> result = new HashMap<>();

        // 현재 로그인한 사용자가 고객으로 참여한 예약 목록 찾기
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException("존재하지 않는 사용자입니다."));
        List<MeetingInfoDTO> meetingListAsUser = new ArrayList<>();
        List<Meeting> meetings = meetingRepository.findByUser_Id(userId);
        for(Meeting meeting : meetings) {
            meetingListAsUser.add(MeetingInfoDTO.of(meeting));
        }
        result.put("meetingListAsUser", meetingListAsUser);

        // 현재 로그인한 사용자가 작가로 등록한 상태라면 (artistId가 0이 아닌 경우)
        if(artistId != 0) {
            Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new BadRequestException("존재하지 않는 작가입니다."));
            List<MeetingInfoDTO> meetingListAsArtist = new ArrayList<>();
            meetings = meetingRepository.findByArtist_Id(artistId);
            for(Meeting meeting : meetings) {
                meetingListAsArtist.add(MeetingInfoDTO.of(meeting));
            }
            result.put("meetingListAsArtist", meetingListAsArtist);
         }

        return result;
    }
}
