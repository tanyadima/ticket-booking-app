package com.booking_app.booking_app.service;

import com.booking_app.booking_app.exceptions.ShowtimeNotFoundException;
import com.booking_app.booking_app.model.Movie;
import com.booking_app.booking_app.model.Showtime;
import com.booking_app.booking_app.repository.MovieRepository;
import com.booking_app.booking_app.repository.ShowtimeRepository;
import com.booking_app.booking_app.dto.ShowtimeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowtimeService {
    @Autowired
    private ShowtimeRepository showtimeRepository;
    @Autowired
    private MovieRepository movieRepository;

    public Showtime addShowtime(ShowtimeRequest showtimeRequest) {
        Movie movie = movieRepository.findByTitle(showtimeRequest.getMovie())
                .orElseThrow(() -> new DataIntegrityViolationException("Movie not found with title: " + showtimeRequest.getMovie()));
        Showtime showtime = new Showtime();
        showtime.setMovie(movie);
        showtime.setTheater(showtimeRequest.getTheater());
        showtime.setStartTime(showtimeRequest.getStartTime());
        showtime.setEndTime(showtimeRequest.getEndTime());

        // Validate overlapping showtimes
        List<Showtime> overlappingShowtimes = showtimeRepository.findOverlappingShowtimes(
                showtime.getTheater(),
                showtime.getStartTime(),
                showtime.getEndTime()
        );

        if (!showtimeRequest.getStartTime().isBefore(showtimeRequest.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }

        if (!overlappingShowtimes.isEmpty()) {
            throw new IllegalArgumentException("Overlapping showtime exist for this theater.");
        }

        return showtimeRepository.save(showtime);
    }

    public List<Showtime> getAllShowtimes() {
        return showtimeRepository.findAll();
    }

    public void deleteShowtimeById(Long id) {
        boolean exists = showtimeRepository.existsById(id);
        if (!exists) {
            throw new ShowtimeNotFoundException("Showtime not found");
        }
        showtimeRepository.deleteById(id);
    }

    public Showtime updateShowtime(Long showtimeId, ShowtimeRequest showtimeRequest) {
        Showtime existingShowtime = showtimeRepository.findById(showtimeId)
                .orElseThrow(() -> new ShowtimeNotFoundException("Showtime not found with ID: " + showtimeId));
        Movie movie = movieRepository.findByTitle(showtimeRequest.getMovie())
                .orElseThrow(() -> new DataIntegrityViolationException("Movie not found with title: " + showtimeRequest.getMovie()));
        if (!showtimeRequest.getStartTime().isBefore(showtimeRequest.getEndTime())) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        Showtime updatedShowtime = new Showtime();
        updatedShowtime.setMovie(movie);
        updatedShowtime.setTheater(showtimeRequest.getTheater());
        updatedShowtime.setStartTime(showtimeRequest.getStartTime());
        updatedShowtime.setEndTime(showtimeRequest.getEndTime());
        // Validate overlapping showtimes
        List<Showtime> overlappingShowtimes = showtimeRepository.findOverlappingShowtimes(
                updatedShowtime.getTheater(),
                updatedShowtime.getStartTime(),
                updatedShowtime.getEndTime()
        );

        if (!overlappingShowtimes.isEmpty()) {
            boolean overlapWithDifferentShowtime = overlappingShowtimes.stream()
                    .anyMatch(showtime -> !showtime.getId().equals(showtimeId));
            if (overlapWithDifferentShowtime) {
                throw new IllegalArgumentException("Overlapping showtime exist for this theater.");
            }
        }

        existingShowtime.setMovie(movie);
        existingShowtime.setTheater(updatedShowtime.getTheater());
        existingShowtime.setStartTime(updatedShowtime.getStartTime());
        existingShowtime.setEndTime(updatedShowtime.getEndTime());

        return showtimeRepository.save(existingShowtime);
    }

    public List<Showtime> getShowtimesByMovie(String movieTitle) {
        Movie movie = movieRepository.findByTitle(movieTitle)
                .orElseThrow(() -> new DataIntegrityViolationException("Movie not found with title: " + movieTitle));
        return showtimeRepository.findByMovieId(movie.getId());
    }

    public List<Showtime> getShowtimesByTheater(String theater) {
        return showtimeRepository.findByTheater(theater);
    }

}
