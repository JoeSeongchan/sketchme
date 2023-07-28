package com.dutaduta.sketchme.member.service;

import com.dutaduta.sketchme.member.dao.ArtistRepository;
import com.dutaduta.sketchme.member.domain.Artist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final ArtistRepository artistRepository;

    public Optional<String> getDescription(Long id) {
        Optional<Artist> artist = artistRepository.findById(id);
        return artist.map(Artist::getDescription);
    }
}
