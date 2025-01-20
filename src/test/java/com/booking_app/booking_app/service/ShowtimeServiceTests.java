package com.booking_app.booking_app.service;

import com.booking_app.booking_app.model.Movie;
import com.booking_app.booking_app.model.Showtime;
import com.booking_app.booking_app.repository.MovieRepository;
import com.booking_app.booking_app.repository.ShowtimeRepository;
import com.booking_app.booking_app.requests.ShowtimeRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShowtimeServiceTest {

    @Mock
    private ShowtimeRepository showtimeRepository;

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private ShowtimeService showtimeService;

    private Movie movie = new Movie(1L, "Movie A", "Action", 120, 8.5, 2023);
    private Showtime getShowtime1(){
        Showtime showtime1 = new Showtime();
        showtime1.setId(1L);
        showtime1.setMovie(movie);
        showtime1.setTheater("Theater A");
        showtime1.setStartTime(LocalDateTime.parse("2025-01-19T10:00:00"));
        showtime1.setEndTime(LocalDateTime.parse("2025-01-19T11:00:00"));
        return showtime1;
    };
    private Showtime getShowtime2(){
        Showtime showtime2 = new Showtime();
        showtime2.setId(null);
        showtime2.setMovie(movie);
        showtime2.setTheater("Theater A");
        showtime2.setStartTime(LocalDateTime.parse("2025-01-19T10:00:00"));
        showtime2.setEndTime(LocalDateTime.parse("2025-01-19T11:00:00"));
        return showtime2;
    };
    private ShowtimeRequest getShowtimeRequest(){
        ShowtimeRequest request = new ShowtimeRequest();
        request.setId(null);
        request.setMovieTitle(movie.getTitle());
        request.setTheater("Theater A");
        request.setStartTime(LocalDateTime.parse("2025-01-19T10:00:00"));
        request.setEndTime(LocalDateTime.parse("2025-01-19T11:00:00"));
        return request;
    };

    @Test
    void testAddShowtime() {
        Showtime showtime = this.getShowtime2();
        Showtime savedShowtime = this.getShowtime1();
        ShowtimeRequest showtimeRequest = this.getShowtimeRequest();

        when(movieRepository.findByTitle(this.movie.getTitle())).thenReturn(Optional.of(this.movie));
        when(showtimeRepository.save(any(Showtime.class))).thenReturn(savedShowtime);

        Showtime result = showtimeService.addShowtime(showtimeRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(movieRepository, times(1)).findByTitle(this.movie.getTitle());
        verify(showtimeRepository, times(1)).save(any(Showtime.class));
    }

    @Test
    void testAddShowtimeThrowsExceptionIfMovieNotFound() {
        Showtime showtime = this.getShowtime2();
        Showtime savedShowtime = this.getShowtime1();
        ShowtimeRequest showtimeRequest = this.getShowtimeRequest();

        assertThrows(RuntimeException.class, () -> showtimeService.addShowtime(showtimeRequest));
        verify(movieRepository, times(1)).findByTitle(this.movie.getTitle());
        verifyNoInteractions(showtimeRepository);
    }

    @Test
    void testUpdateShowtime() {
        Showtime existingShowtime = this.getShowtime1();
        Showtime updatedShowtime = this.getShowtime2();
        ShowtimeRequest showtimeRequest = this.getShowtimeRequest();

        when(showtimeRepository.findById(1L)).thenReturn(Optional.of(existingShowtime));
        when(showtimeRepository.save(existingShowtime)).thenReturn(updatedShowtime);
        when(movieRepository.findByTitle(this.movie.getTitle())).thenReturn(Optional.of(this.movie));

        Showtime result = showtimeService.updateShowtime(1L, showtimeRequest);

        assertNotNull(result);
        assertEquals(updatedShowtime.getStartTime(), result.getStartTime());
        assertEquals(updatedShowtime.getEndTime(), result.getEndTime());
        verify(showtimeRepository, times(1)).findById(1L);
        verify(showtimeRepository, times(1)).save(existingShowtime);
    }

    @Test
    void testDeleteShowtime() {
        Long showtimeId = 1L;

        when(showtimeRepository.existsById(showtimeId)).thenReturn(true);
        doNothing().when(showtimeRepository).deleteById(showtimeId);

        showtimeService.deleteShowtimeById(showtimeId);

        verify(showtimeRepository, times(1)).existsById(showtimeId);
        verify(showtimeRepository, times(1)).deleteById(showtimeId);
    }

    @Test
    void testGetShowtimesByMovie() {
        Long movieId = 1L;
        List<Showtime> showtimes = Arrays.asList(
                this.getShowtime2()
        );

        when(showtimeRepository.findByMovieId(movieId)).thenReturn(showtimes);
        when(movieRepository.findByTitle(this.movie.getTitle())).thenReturn(Optional.ofNullable(this.movie));
        List<Showtime> result = showtimeService.getShowtimesByMovie(this.movie.getTitle());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(showtimeRepository, times(1)).findByMovieId(movieId);
    }

    @Test
    void testGetShowtimesByTheater() {
        List<Showtime> showtimes = Arrays.asList(
                this.getShowtime2()
        );

        when(showtimeRepository.findByTheater(this.getShowtime2().getTheater())).thenReturn(showtimes);

        List<Showtime> result = showtimeService.getShowtimesByTheater(this.getShowtime2().getTheater());

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(showtimeRepository, times(1)).findByTheater(this.getShowtime2().getTheater());
    }
}
