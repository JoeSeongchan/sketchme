package com.dutaduta.sketchme.meeting.dao;

import com.dutaduta.sketchme.meeting.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Meeting findByIdAndArtist_Id(Long id, Long artistId);
}
