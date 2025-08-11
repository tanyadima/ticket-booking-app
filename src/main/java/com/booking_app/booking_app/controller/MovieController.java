package com.booking_app.booking_app.controller;

import com.booking_app.booking_app.exceptions.MovieNotFoundException;
import com.booking_app.booking_app.model.Movie;
import com.booking_app.booking_app.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/movie")
@PreAuthorize("hasRole('ADMIN')")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie, UriComponentsBuilder uriBuilder) {
        Movie savedMovie = movieService.addMovie(movie);
        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<>(savedMovie, headers, HttpStatus.CREATED); // Return 201 Created
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        movieService.updateMovie(id, movie);
        HttpHeaders headers = new HttpHeaders();
        movie.setId(id);
        return new ResponseEntity<>(movie, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovieById(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }


    @GetMapping("/search")
    public ResponseEntity<List<Movie>> searchMovies(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer releaseYear) {
        List<Movie> movies = movieService.searchMovies( genre, releaseYear);
        if(!movies.isEmpty()){
            return ResponseEntity.ok(movies);
        }
        throw new MovieNotFoundException("Movie not found");
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Movie> getMovieByTitle(@PathVariable String title) {
        Movie movie = movieService.findMovieByTitle(title);
        return ResponseEntity.ok(movie);
    }
}
