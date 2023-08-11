package com.dutaduta.sketchme.chat.service;


import com.dutaduta.sketchme.chat.dao.ChatRepository;
import com.dutaduta.sketchme.chat.dao.ChatRoomCustomRepository;
import com.dutaduta.sketchme.chat.dao.ChatRoomRepository;
import com.dutaduta.sketchme.chat.domain.Chat;
import com.dutaduta.sketchme.chat.domain.ChatRoom;
import com.dutaduta.sketchme.chat.dto.BunchOfChatRoomRequestDTO;
import com.dutaduta.sketchme.chat.dto.BunchOfChatRoomResponseDTO;
import com.dutaduta.sketchme.chat.dto.CreateOrGetRoomRequestDTO;
import com.dutaduta.sketchme.chat.dto.CreateOrGetRoomResponseDTO;
import com.dutaduta.sketchme.member.constant.MemberType;
import com.dutaduta.sketchme.member.dao.ArtistCustomRepository;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import com.dutaduta.sketchme.member.dao.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomCustomRepository chatRoomCustomRepository;
    private final UserRepository userRepository;
    private final ArtistCustomRepository artistCustomRepository;
    private final ChatRepository chatRepository;

    public CreateOrGetRoomResponseDTO createRoomOrGetExistedRoom(CreateOrGetRoomRequestDTO request) {
        Optional<User> requestUser = userRepository.findById(request.getRequestUserID());
        Artist artist = artistCustomRepository.findArtistByUserId(request.getUserIDOfArtist());
        ChatRoom chatRoom = ChatRoom.createRoom(requestUser.get(), artist);
        Optional<ChatRoom> alreadyExist = chatRoomRepository.findByUserAndArtist(requestUser.get(), artist);

        if(alreadyExist.isEmpty()) {
            ChatRoom createdChatRoom = chatRoomRepository.save(chatRoom);
            Chat chat = Chat.builder()
                    .chatRoom(createdChatRoom)
                    .sender(requestUser.get())
                    .receiver(artist.getUser())
                    .memberType(MemberType.FIRST_COMMENT)
                    .content("")
                    .build();

            chat = chatRepository.save(chat);
            chat.setChatRoom(chatRoom);
            //변환로직 작성
            return CreateOrGetRoomResponseDTO.toDTO(createdChatRoom);
        }
        //변환로직 작성
        return CreateOrGetRoomResponseDTO.toDTO(alreadyExist.get());
    }

    public List<BunchOfChatRoomResponseDTO> getBunchOfChatRoom(BunchOfChatRoomRequestDTO getBunchOfChatRoomRequest) {
        List<ChatRoom> chatRooms =
                chatRoomCustomRepository.findBunchOfChatRoomByUser(getBunchOfChatRoomRequest.getUserID(),
                        getBunchOfChatRoomRequest.getMemberType());
        List<BunchOfChatRoomResponseDTO> bunchOfDto = new ArrayList<>();
        for (ChatRoom room : chatRooms) { //비효율적인 구간. refactoring 필수. DTO 어느정도 안정화되면 QueryDSL로 커스텀
            bunchOfDto.add(BunchOfChatRoomResponseDTO.toDTO(room, getBunchOfChatRoomRequest.getMemberType()));
        }
        return bunchOfDto;
    }
}
