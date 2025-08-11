package com.booking_app.booking_app.controller;

import com.booking_app.booking_app.dto.ShowtimeDto;
import com.booking_app.booking_app.exceptions.ShowtimeNotFoundException;
import com.booking_app.booking_app.model.Showtime;
import com.booking_app.booking_app.dto.ShowtimeRequest;
import com.booking_app.booking_app.service.ShowtimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/showtime")
@PreAuthorize("hasRole('ADMIN')")
public class ShowtimeController {
    @Autowired
    private ShowtimeService showtimeService;

    @PostMapping
    public ResponseEntity<ShowtimeDto> addShowtime(@RequestBody ShowtimeRequest showtimeRequest) {
        Showtime createdShowtime = showtimeService.addShowtime(showtimeRequest);
        ShowtimeDto showtimeDto = new ShowtimeDto(createdShowtime.getId(), createdShowtime.getStartTime(), createdShowtime.getEndTime(), createdShowtime.getMovie().getTitle(), createdShowtime.getTheater());
        return ResponseEntity.status(HttpStatus.CREATED).body(showtimeDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShowtimeRequest> updateShowtime(@PathVariable Long id, @RequestBody ShowtimeRequest showtimeRequest) {
        showtimeService.updateShowtime(id, showtimeRequest);
        HttpHeaders headers = new HttpHeaders();
        showtimeRequest.setId(id);
        return new ResponseEntity<>(showtimeRequest, headers, HttpStatus.OK);
    }

    @GetMapping
    public List<ShowtimeDto> getAllMovies() {
        List<Showtime> showtimes = showtimeService.getAllShowtimes();
        List<ShowtimeDto> showtimeDtos = showtimes.stream()
                .map(showtime -> new ShowtimeDto(
                        showtime.getId(),
                        showtime.getStartTime(),
                        showtime.getEndTime(),
                        showtime.getMovie().getTitle(),
                        showtime.getTheater()
                ))
                .toList();
        return showtimeDtos;
    }

    @GetMapping("/theater")
    public ResponseEntity<List<ShowtimeDto>> getShowtimesByTheater(@RequestParam("theater") String theater) {
        List<Showtime> showtimes = showtimeService.getShowtimesByTheater(theater);
        if(!showtimes.isEmpty()){
            List<ShowtimeDto> showtimeDtos = showtimes.stream()
                    .map(showtime -> new ShowtimeDto(
                            showtime.getId(),
                            showtime.getStartTime(),
                            showtime.getEndTime(),
                            showtime.getMovie().getTitle(),
                            showtime.getTheater()
                    ))
                    .toList();
            return ResponseEntity.ok(showtimeDtos);
        }
        throw new ShowtimeNotFoundException("Showtime not found");
    }

    @GetMapping("/movie")
    public ResponseEntity<List<ShowtimeDto>> getShowtimesByMovie(@RequestParam("movieTitle") String movieTitle) {
        List<Showtime> showtimes = showtimeService.getShowtimesByMovie(movieTitle);
        if(!showtimes.isEmpty()){
            List<ShowtimeDto> showtimeDtos = showtimes.stream()
                    .map(showtime -> new ShowtimeDto(
                            showtime.getId(),
                            showtime.getStartTime(),
                            showtime.getEndTime(),
                            showtime.getMovie().getTitle(),
                            showtime.getTheater()
                    ))
                    .toList();
            return ResponseEntity.ok(showtimeDtos);
        }
        throw new ShowtimeNotFoundException("Showtime not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtimeById(id);
        return ResponseEntity.status(HttpStatus.OK).build(); // Return 200 No Content on successful deletion
    }
}

