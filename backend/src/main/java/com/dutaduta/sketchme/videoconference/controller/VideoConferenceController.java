package com.dutaduta.sketchme.videoconference.controller;

import com.dutaduta.sketchme.global.ResponseFormat;
import com.dutaduta.sketchme.oidc.dto.UserInfoInAccessTokenDTO;
import com.dutaduta.sketchme.oidc.jwt.JwtUtil;
//import com.dutaduta.sketchme.videoconference.controller.response.*;
import com.dutaduta.sketchme.videoconference.service.VideoConferenceService;
import com.dutaduta.sketchme.videoconference.service.response.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class VideoConferenceController {
    private final VideoConferenceService videoConferenceService;
    private final JwtUtil jwtUtil;

    @PostMapping("meeting/{meetingId}/videoconference/room")
    public ResponseEntity<ResponseFormat<SessionGetResponse>> openRoom(@PathVariable("meetingId")long meetingId, HttpServletRequest request){
        UserInfoInAccessTokenDTO userInfo= jwtUtil.extractUserInfo(request);
        SessionGetResponse responseDTO = videoConferenceService.makeSession(userInfo, meetingId);
        return ResponseFormat.success(responseDTO).toEntity();
    }

    @PostMapping("meeting/{meetingId}/videoconference/connection")
    public ResponseEntity<ResponseFormat<ConnectionCreateResponse>> getConnection(@PathVariable("meetingId")long meetingId, HttpServletRequest request){
        UserInfoInAccessTokenDTO userInfo= jwtUtil.extractUserInfo(request);
        ConnectionCreateResponse responseDTO = videoConferenceService.createConnection(meetingId,userInfo);
        return ResponseFormat.success(responseDTO).toEntity();
    }

    @DeleteMapping("meeting/{meetingId}/videoconference/room")
    public ResponseEntity<ResponseFormat<Object>> closeRoom(@PathVariable("meetingId") long meetingId, HttpServletRequest request){
        UserInfoInAccessTokenDTO userInfo = jwtUtil.extractUserInfo(request);
        videoConferenceService.closeRoom(meetingId, userInfo);
        return ResponseFormat.success().toEntity();
    }
}
