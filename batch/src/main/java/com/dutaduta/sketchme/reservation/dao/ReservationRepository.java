package com.dutaduta.sketchme.reservation.dao;

import com.dutaduta.sketchme.reservation.domain.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Meeting, Long> {
}
