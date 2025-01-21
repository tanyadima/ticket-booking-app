package com.booking_app.booking_app.controller;

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
    public ResponseEntity<Showtime> addShowtime(@RequestBody ShowtimeRequest showtimeRequest) {
        Showtime createdShowtime = showtimeService.addShowtime(showtimeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdShowtime);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShowtimeRequest> updateShowtime(@PathVariable Long id, @RequestBody ShowtimeRequest showtimeRequest) {
        showtimeService.updateShowtime(id, showtimeRequest);
        HttpHeaders headers = new HttpHeaders();
        showtimeRequest.setId(id);
        return new ResponseEntity<>(showtimeRequest, headers, HttpStatus.OK);
    }

    @GetMapping
    public List<Showtime> getAllMovies() {
        return showtimeService.getAllShowtimes();
    }

    @GetMapping("/theater")
    public ResponseEntity<List<Showtime>> getShowtimesByTheater(@RequestParam("theater") String theater) {
        List<Showtime> showtimes = showtimeService.getShowtimesByTheater(theater);
        if(!showtimes.isEmpty()){
            return ResponseEntity.ok(showtimes);
        }
        throw new ShowtimeNotFoundException("Showtime not found");
    }

    @GetMapping("/movie")
    public ResponseEntity<List<Showtime>> getShowtimesByMovie(@RequestParam("movieTitle") String movieTitle) {
        List<Showtime> showtimes = showtimeService.getShowtimesByMovie(movieTitle);
        if(!showtimes.isEmpty()){
            return ResponseEntity.ok(showtimes);
        }
        throw new ShowtimeNotFoundException("Showtime not found");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtimeById(id);
        return ResponseEntity.status(HttpStatus.OK).build(); // Return 200 No Content on successful deletion
    }
}

