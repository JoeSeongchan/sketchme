package com.dutaduta.sketchme.member.dao;

import com.dutaduta.sketchme.common.domain.Hashtag;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.member.domain.ArtistHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistHashtagRepository extends JpaRepository<ArtistHashtag, Long> {

    ArtistHashtag findByArtistAndHashtag(Artist artist, Hashtag hashtag);
}
