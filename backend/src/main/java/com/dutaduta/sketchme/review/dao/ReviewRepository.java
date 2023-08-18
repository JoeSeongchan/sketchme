package com.dutaduta.sketchme.review.dao;

import com.dutaduta.sketchme.meeting.domain.Meeting;
import com.dutaduta.sketchme.member.domain.Artist;
import com.dutaduta.sketchme.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Boolean existsByMeetingId(Long meetingId);


    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.artist = :artist")
    BigDecimal calculateAvgRating(@Param("artist") Artist artist);

    Review findByMeeting(Meeting meeting);
}
