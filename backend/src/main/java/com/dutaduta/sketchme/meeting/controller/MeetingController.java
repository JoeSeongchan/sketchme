package com.dutaduta.sketchme.meeting.controller;

import com.dutaduta.sketchme.global.ResponseFormat;
import com.dutaduta.sketchme.meeting.dto.DeterminateMeetingRequest;
import com.dutaduta.sketchme.meeting.dto.MeetingInfoDTO;
import com.dutaduta.sketchme.meeting.dto.ReservationDTO;
import com.dutaduta.sketchme.meeting.service.MeetingService;
import com.dutaduta.sketchme.oidc.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class MeetingController {
    private final MeetingService meetingService;

    @PostMapping("/meeting")
    public ResponseEntity<ResponseFormat<Long>> makeReservation(
            @RequestBody ReservationDTO reservationDto,
            HttpServletRequest request
    ) {
        String secretKey = JwtProvider.getSecretKey();
        String token = JwtProvider.resolveToken(request);
        reservationDto.setUserID(JwtProvider.getUserId(token, secretKey));  //유저의 요청이므로
        Long meetingId = meetingService.createMeeting(reservationDto);
        return ResponseFormat.success(meetingId).toEntity();
    }

    @GetMapping("/meeting/{id}")
    public ResponseEntity<ResponseFormat<MeetingInfoDTO>> getMeetingInformation(
            @PathVariable Long id,
            HttpServletRequest request
    ) {
        //나중에 보안로직 처리해야함.
        String secretKey = JwtProvider.getSecretKey();
        String token = JwtProvider.resolveToken(request);
        MeetingInfoDTO meetingInfoDto = meetingService.getMeetingInformation(id);
        return ResponseFormat.success(meetingInfoDto).toEntity();
    }

    @PutMapping("/meeting")
    public ResponseEntity<?> determinateMeeting(
            @RequestBody @Valid DeterminateMeetingRequest determinateMeetingRequest,
            HttpServletRequest request
    ) {
        String secretKey = JwtProvider.getSecretKey();
        String token = JwtProvider.resolveToken(request);
        determinateMeetingRequest.setArtistID(JwtProvider.getArtistId(token, secretKey)); //artist의 요청이므로
        meetingService.determinate(determinateMeetingRequest);
        return ResponseFormat.success("").toEntity();
    }

    @GetMapping("/meeting/list")
    public ResponseEntity<ResponseFormat<Map<String, List<MeetingInfoDTO>>>> getMyMeetingList(HttpServletRequest request) {
        Long artistId = JwtProvider.getArtistId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        Long userId = JwtProvider.getUserId(JwtProvider.resolveToken(request), JwtProvider.getSecretKey());
        Map<String, List<MeetingInfoDTO>> meetingLists = meetingService.getMyMeetingList(userId, artistId);
        return ResponseFormat.success(meetingLists).toEntity();
    }

}
