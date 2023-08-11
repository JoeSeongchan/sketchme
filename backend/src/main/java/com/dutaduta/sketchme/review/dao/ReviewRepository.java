package com.dutaduta.sketchme.review.dao;

import com.dutaduta.sketchme.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Boolean existsByMeetingId(Long meetingId);
}
