package com.dutaduta.sketchme.meeting.dao;

import com.dutaduta.sketchme.meeting.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    Meeting findByIdAndArtist_Id(Long id, Long artistId);

    List<Meeting> findByUser_Id(Long userId);

    List<Meeting> findByArtist_Id(Long artistId);

}
