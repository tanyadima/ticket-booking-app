package com.booking_app.booking_app.repository;

import com.booking_app.booking_app.model.Showtime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    @Query("SELECT s FROM Showtime s WHERE s.theater = :theater AND " +
            "(:startTime > s.startTime AND :startTime < s.endTime OR " +
            ":endTime > s.startTime AND :endTime < s.endTime OR " +
            "s.startTime > :startTime AND s.startTime < :endTime)")
    List<Showtime> findOverlappingShowtimes(
            @Param("theater") String theater,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);

    List<Showtime> findByMovieId(Long movieId);

    List<Showtime> findByTheater(String theater);
}