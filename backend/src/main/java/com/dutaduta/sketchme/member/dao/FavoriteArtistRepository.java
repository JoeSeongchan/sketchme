package com.dutaduta.sketchme.member.dao;

import com.dutaduta.sketchme.member.domain.FavoriteArtist;
import com.dutaduta.sketchme.member.domain.OAuthType;
import com.dutaduta.sketchme.member.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteArtistRepository extends JpaRepository<FavoriteArtist, Long> {

    boolean existsByUserIdAndArtist_Id(Long userId, Long ArtistId);

    List<FavoriteArtist> findByUser_Id(Long userId);

    FavoriteArtist findByUser_IdAndArtist_Id(Long userId, Long ArtistId);
}
