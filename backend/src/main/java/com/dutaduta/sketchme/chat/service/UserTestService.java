package com.dutaduta.sketchme.chat.service;

import com.dutaduta.sketchme.chat.dao.CreateUserTestRepository;
import com.dutaduta.sketchme.global.exception.BusinessException;
import com.dutaduta.sketchme.member.dao.ArtistRepository;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
@Log4j2
public class UserTestService {

    private final ArtistRepository artistRepository;
    private final CreateUserTestRepository createUserTestRepository;

    public void createArtistTest(Artist artist, Long userId) {
        Optional<User> user = createUserTestRepository.findById(userId);
//        log.info("USER" + user.toString());

        if(user.isEmpty()) throw new BusinessException();

        Artist toSave = Artist.builder()
                .user(user.get())
                .description(artist.getDescription())
                .build();
//        log.info(toSave.toString());
        Artist artist1 = artistRepository.save(toSave);
        user.get().setArtist(artist1);
    }
}
