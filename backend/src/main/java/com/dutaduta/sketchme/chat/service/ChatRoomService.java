package com.dutaduta.sketchme.chat.service;


import com.dutaduta.sketchme.chat.dao.ChatRepository;
import com.dutaduta.sketchme.chat.dao.ChatRoomCustomRepository;
import com.dutaduta.sketchme.chat.dao.ChatRoomRepository;
import com.dutaduta.sketchme.chat.domain.Chat;
import com.dutaduta.sketchme.chat.domain.ChatRoom;
import com.dutaduta.sketchme.chat.dto.BunchOfChatRoomRequestDTO;
import com.dutaduta.sketchme.chat.dto.BunchOfChatRoomResponseDTO;
import com.dutaduta.sketchme.chat.dto.CreateOrGetRoomRequestDTO;
import com.dutaduta.sketchme.global.exception.BadRequestException;
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

    public ChatRoom createRoomOrGetExistedRoom(CreateOrGetRoomRequestDTO request) {
        //유저를 찾는다. 유저가 로그인했다는 것은 삭제되지 않았다는 상태이므로 따로 검사하지 않는다
        User requestUser = userRepository.findById(request.getRequestUserID())
                .orElseThrow(() -> new BadRequestException("잘못된 요청입니다"));
        Artist artist = artistCustomRepository.findArtistByUserIdAndNotDeactivated(request.getUserIDOfArtist());
        Optional<ChatRoom> alreadyExist = chatRoomRepository.findByUserAndArtist(requestUser, artist);
        if(alreadyExist.isEmpty()) { //if문 변경 필요
            ChatRoom chatRoom = ChatRoom.createRoom(requestUser, artist);
            ChatRoom createdChatRoom = chatRoomRepository.save(chatRoom);
            Chat chat = Chat.builder()
                    .chatRoom(createdChatRoom)
                    .sender(requestUser)
                    .receiver(artist.getUser())
                    .memberType(MemberType.FIRST_COMMENT)
                    .content("")
                    .build();

            chat = chatRepository.save(chat);
            chat.setChatRoom(chatRoom);
            //변환로직 작성
            return createdChatRoom;
        }
        //변환로직 작성
        return alreadyExist.get();
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
