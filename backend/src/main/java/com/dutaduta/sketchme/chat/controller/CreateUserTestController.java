package com.dutaduta.sketchme.chat.controller;

import com.dutaduta.sketchme.chat.dao.CreateUserTestRepository;
import com.dutaduta.sketchme.chat.service.UserTestService;
import com.dutaduta.sketchme.member.dao.ArtistRepository;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.User;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@AllArgsConstructor
@Slf4j
public class CreateUserTestController {
    private final CreateUserTestRepository createUserTestRepository;
    private final UserTestService userTestService;

    @PostMapping("/test/create/user")
    public User createUser(@RequestBody User user) {
        log.info(user.toString());
        User user1 = createUserTestRepository.save(user);
        log.info(user1.toString());
        return user1;
    }

    @PostMapping("/test/create/user/{userId}")
    public String createArtist(@RequestBody Artist artist, @PathVariable("userId") Long userId) {
        userTestService.createArtistTest(artist, userId);
        return "SUCCESS";
    }
}