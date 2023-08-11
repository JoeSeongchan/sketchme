package com.dutaduta.sketchme.meeting.service;

import com.dutaduta.sketchme.chat.constant.KafkaConstants;
import com.dutaduta.sketchme.chat.dao.ChatRoomRepository;
import com.dutaduta.sketchme.chat.domain.ChatRoom;
import com.dutaduta.sketchme.chat.dto.MessageDTO;
import com.dutaduta.sketchme.common.dao.CategoryRepository;
import com.dutaduta.sketchme.common.domain.Category;
import com.dutaduta.sketchme.global.exception.BadRequestException;
import com.dutaduta.sketchme.meeting.dao.MeetingRepository;
import com.dutaduta.sketchme.meeting.domain.InfoMessageFormatter;
import com.dutaduta.sketchme.meeting.domain.Meeting;
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

@Service
@Log4j2
@Transactional
@AllArgsConstructor
public class MeetingService {

    private final MeetingRepository meetingRepository;

    private final UserRepository userRepository;

    private final ArtistRepository artistRepository;

    private final CategoryRepository categoryRepository;
    private final ChatRoomRepository chatRoomRepository;

    private final KafkaTemplate<String, MessageDTO> kafkaTemplate;

    @Transactional
    public Long createMeeting(ReservationDTO reservationDto) {
        User user = userRepository.getReferenceById(reservationDto.getUserID());
        Artist artist = artistRepository.getReferenceById(reservationDto.getArtistID());
        Category category = categoryRepository.getReferenceById(reservationDto.getCategoryID());
        //채팅방 가져오기
        ChatRoom chatRoom = chatRoomRepository.findByUserAndArtist(user, artist)
                .orElseThrow(() -> new BadRequestException("채팅방을 찾을 수 없습니다"));
        Meeting meeting = Meeting.createMeeting(user, artist, category, reservationDto, chatRoom);
        Meeting savedMeeting = meetingRepository.save(meeting);
        String content = InfoMessageFormatter.create(savedMeeting, MemberType.BOT_RESERVATION);
        MessageDTO messageDTO = MessageDTO.of(meeting, content);
        kafkaTemplate.send(KafkaConstants.KAFKA_MEETING, messageDTO.getSenderID().toString(), messageDTO);
        kafkaTemplate.send(KafkaConstants.KAFKA_MEETING, messageDTO.getReceiverID().toString(), messageDTO);
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
        log.info(messageDTO);
        kafkaTemplate.send(KafkaConstants.KAFKA_MEETING, messageDTO.getSenderID().toString(), messageDTO);
    }
}
