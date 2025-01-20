package com.booking_app.booking_app.repository;

import com.booking_app.booking_app.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    @Query("SELECT s FROM Showtime s WHERE s.theater = :theater AND " +
            "(:startTime BETWEEN s.startTime AND s.endTime OR " +
            ":endTime BETWEEN s.startTime AND s.endTime OR " +
            "s.startTime BETWEEN :startTime AND :endTime)")
    List<Showtime> findOverlappingShowtimes(
            @Param("theater") String theater,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    // Find all showtimes for a specific movie
    List<Showtime> findByMovieId(Long movieId);

    // Find all showtimes for a specific theater
    List<Showtime> findByTheater(String theater);
}