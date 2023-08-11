package com.dutaduta.sketchme.chat.dao;

import com.dutaduta.sketchme.chat.domain.ChatRoom;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUserAndArtist(User user, Artist artist);
}
