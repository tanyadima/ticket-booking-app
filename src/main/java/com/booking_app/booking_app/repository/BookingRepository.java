package com.booking_app.booking_app.repository;

import com.booking_app.booking_app.model.Booking;
import com.booking_app.booking_app.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByUserId(Long userId);

    boolean existsByShowtimeAndSeatNumber(Showtime showtime, Integer seatNumber);

    Integer countByShowtime(Showtime showtime);
}
