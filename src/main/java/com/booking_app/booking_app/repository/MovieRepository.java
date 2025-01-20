package com.booking_app.booking_app.repository;

import com.booking_app.booking_app.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findByGenreContainingIgnoreCase(String genre);
    List<Movie> findByReleaseYear(Integer releaseYear);
    boolean existsByTitle(String title);
    Optional<Movie> findByTitle(String title);
}
