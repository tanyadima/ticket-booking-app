package com.booking_app.booking_app.repository;

import com.booking_app.booking_app.model.Booking;
import com.booking_app.booking_app.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Find all bookings for a specific user
    List<Booking> findByUserId(Long userId);

    // Find all bookings for a specific showtime
    List<Booking> findByShowtimeId(Long showtimeId);

    // Check if a seat is already booked
    boolean existsByShowtimeAndSeatNumber(Showtime showtime, Integer seatNumber);

    // Count tickets for a specific showtime
    Integer countByShowtime(Showtime showtime);
}
