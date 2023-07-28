package com.dutaduta.sketchme.member.dao;


import com.dutaduta.sketchme.member.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    
}
