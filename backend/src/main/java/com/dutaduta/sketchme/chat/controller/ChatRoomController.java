package com.dutaduta.sketchme.chat.controller;


import com.dutaduta.sketchme.chat.domain.ChatRoom;
import com.dutaduta.sketchme.chat.dto.BunchOfChatRoomRequestDTO;
import com.dutaduta.sketchme.chat.dto.BunchOfChatRoomResponseDTO;
import com.dutaduta.sketchme.chat.dto.CreateOrGetRoomRequestDTO;
import com.dutaduta.sketchme.chat.dto.CreateOrGetRoomResponseDTO;
import com.dutaduta.sketchme.chat.service.ChatRoomService;
import com.dutaduta.sketchme.global.ResponseFormat;
import com.dutaduta.sketchme.oidc.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/chatroom/get")
    public ResponseEntity<ResponseFormat<CreateOrGetRoomResponseDTO>>
    createRoom(@RequestBody @Valid CreateOrGetRoomRequestDTO createOrGetRoomRequestDTO,
               HttpServletRequest request) {
        String secretKey = JwtProvider.getSecretKey();
        String token = JwtProvider.resolveToken(request);
        createOrGetRoomRequestDTO.setRequestUserID(JwtProvider.getUserId(token, secretKey));
        ChatRoom chatRoom =
                chatRoomService.createRoomOrGetExistedRoom(createOrGetRoomRequestDTO);
        log.info(chatRoom);
        return ResponseFormat.success(CreateOrGetRoomResponseDTO.toDTO(chatRoom)).toEntity();
    }

    @GetMapping("/chatroom/list")
    public ResponseEntity<ResponseFormat<List<BunchOfChatRoomResponseDTO>>>
    getBunchOfChatRoom(@ModelAttribute @Valid BunchOfChatRoomRequestDTO getBunchOFChatRoomRequest,
                       HttpServletRequest request) {
        String secretKey = JwtProvider.getSecretKey();
        String token = JwtProvider.resolveToken(request);
        getBunchOFChatRoomRequest.setUserID(JwtProvider.getUserId(token, secretKey));
        List<BunchOfChatRoomResponseDTO> list = chatRoomService.getBunchOfChatRoom(getBunchOFChatRoomRequest);
        return ResponseFormat.success(list).toEntity();
    }
}
