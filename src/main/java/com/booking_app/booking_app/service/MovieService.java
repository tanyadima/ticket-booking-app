package com.booking_app.booking_app.service;

import com.booking_app.booking_app.exceptions.MovieNotFoundException;
import com.booking_app.booking_app.exceptions.ShowtimeNotFoundException;
import com.booking_app.booking_app.model.Movie;
import com.booking_app.booking_app.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    public Movie addMovie(Movie movie) {
        if (movieRepository.existsByTitle(movie.getTitle())) {
            throw new IllegalArgumentException("Movie title must be unique.");
        }
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, Movie updatedMovie) {
        boolean exists = movieRepository.existsById(id);
        if (!exists) {
            throw new MovieNotFoundException("Movie not found");
        }
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        if(!existingMovie.getTitle().equals(updatedMovie.getTitle())){
            throw new IllegalArgumentException("Movie title must be unique");
        }
        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setGenre(updatedMovie.getGenre());
        existingMovie.setDuration(updatedMovie.getDuration());
        existingMovie.setRating(updatedMovie.getRating());
        existingMovie.setReleaseYear(updatedMovie.getReleaseYear());
        return movieRepository.save(existingMovie);
    }

    public void deleteMovie(Long id) {
        boolean exists = movieRepository.existsById(id);
        if (!exists) {
            throw new MovieNotFoundException("Movie not found");
        }
        movieRepository.deleteById(id);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
    }

    public List<Movie> searchMovies(String genre, Integer releaseYear) {
         if (genre != null) {
            return movieRepository.findByGenreContainingIgnoreCase(genre);
        } else if (releaseYear != null) {
            return movieRepository.findByReleaseYear(releaseYear);
        } else {
            return movieRepository.findAll();
        }
    }

    public boolean existsById(Long id) {

        return movieRepository.existsById(id);
    }

    public void deleteMovieById(Long id) {
        boolean exists = movieRepository.existsById(id);
        if (!exists) {
            throw new MovieNotFoundException("Movie not found");
        }
        movieRepository.deleteById(id);
    }

    public Movie findMovieByTitle(String title) {
        return movieRepository.findByTitle(title)
                .orElseThrow(() -> new MovieNotFoundException("Movie with title '" + title + "' not found."));
    }
}
