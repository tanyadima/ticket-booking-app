package com.booking_app.booking_app.service;

import com.booking_app.booking_app.model.Movie;
import com.booking_app.booking_app.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    void testAddMovie() {
        Movie movie = new Movie(null, "Terminator", "Sci-Fi", 150, 9.0, 1985);
        Movie savedMovie = new Movie(1L, "Terminator", "Sci-Fi", 150, 9.0, 1985);

        when(movieRepository.save(movie)).thenReturn(savedMovie);
        Movie result = movieService.addMovie(movie);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    void testUpdateMovie() {
        Long movieId = 1L;
        String title = "Terminator 2";
        Movie existingMovie = new Movie(movieId, "Terminator", "Sci-Fi", 150, 9.0, 1985);
        Movie updatedMovie = new Movie(movieId, "Terminator 2", "Sci-Fi", 169, 8.6, 1989);
        when(movieRepository.save(any(Movie.class))).thenReturn(updatedMovie);
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(updatedMovie));
        when(movieRepository.existsById(movieId)).thenReturn(true);

        Movie result = movieService.updateMovie(movieId, updatedMovie);

        assertNotNull(result);
        assertEquals("Terminator 2", result.getTitle());
        assertEquals(1989, result.getReleaseYear());
        verify(movieRepository, times(1)).save(updatedMovie);
    }

    @Test
    void testUpdateMovieThrowsExceptionIfNotFound() {
        Long movieId = 1L;
        Movie updatedMovie = new Movie(movieId, "Terminator 2", "Sci-Fi", 169, 8.6, 1989);

        when(movieRepository.existsById(movieId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> movieService.updateMovie(movieId, updatedMovie));
        verify(movieRepository, times(1)).existsById(movieId);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void testDeleteMovie() {
        Long movieId = 1L;
        when(movieRepository.existsById(movieId)).thenReturn(true);
        doNothing().when(movieRepository).deleteById(movieId);

        movieService.deleteMovie(movieId);

        verify(movieRepository, times(1)).deleteById(movieId);
    }

    @Test
    void testDeleteMovieThrowsExceptionIfNotFound() {
        Long movieId = 1L;
        when(movieRepository.existsById(movieId)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> movieService.deleteMovie(movieId));
        verify(movieRepository, times(1)).existsById(movieId);
        verify(movieRepository, never()).deleteById(movieId);
    }

    @Test
    void testGetMovieByTitle() {
        Long movieId = 1L;
        String title = "film";
        Movie movie = new Movie(movieId, title, "Sci-Fi", 148, 8.8, 2010);

        when(movieRepository.findByTitle(title)).thenReturn(Optional.of(movie));

        Movie result = movieService.findMovieByTitle(title);

        assertNotNull(result);
        assertEquals(title, result.getTitle());
        verify(movieRepository, times(1)).findByTitle(title);
    }

    @Test
    void testGetMovieByTitleThrowsExceptionIfNotFound() {
        Long movieId = 1L;
        String title = "film";
        when(movieRepository.findByTitle(title)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> movieService.findMovieByTitle(title));
        verify(movieRepository, times(1)).findByTitle(title);
    }

    @Test
    void testGetAllMovies() {
        List<Movie> movies = Arrays.asList(
                new Movie(null, "Terminator", "Sci-Fi", 150, 9.0, 1985),
                new Movie(1L, "Terminator", "Sci-Fi", 150, 9.0, 1985)
        );

        when(movieRepository.findAll()).thenReturn(movies);

        List<Movie> result = movieService.getAllMovies();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(movieRepository, times(1)).findAll();
    }
}
