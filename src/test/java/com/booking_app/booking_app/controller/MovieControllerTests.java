package com.booking_app.booking_app.controller;

import com.booking_app.booking_app.model.Movie;
import com.booking_app.booking_app.service.MovieService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({MovieController.class})
public class MovieControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;


    @Test
    public void testGetAllMovies() throws Exception {
        Movie movie1 = new Movie(1L, "Movie A", "Action", 120, 8.5, 2023);
        Movie movie2 = new Movie(2L, "Movie B", "Drama", 140, 9.0, 2022);

        when(movieService.getAllMovies()).thenReturn(Arrays.asList(movie1, movie2));

        mockMvc.perform(get("/movie")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                       )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is("Movie A")))
                .andExpect(jsonPath("$[1].title", is("Movie B")));
    }

    @Test
    public void testAddMovie() throws Exception {
        Movie movie = new Movie(1L, "Movie A", "Action", 120, 8.5, 2023);

        when(movieService.addMovie(Mockito.any(Movie.class))).thenReturn(movie);

        mockMvc.perform(post("/movie")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"title\": \"Movie A\"," +
                                "\"genre\": \"Action\"," +
                                "\"duration\": 120," +
                                "\"rating\": 8.5," +
                                "\"releaseYear\": 2023}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("Movie A")));
    }

    @Test
    public void updateMovie() throws Exception {
        Movie existingMovie = new Movie(1L, "Inception", "Sci-Fi", 148, 8.8, 2010);
        Movie updatedMovie = new Movie(1L, "Inception 2", "Sci-Fi", 150, 9.0, 2025);

        when(movieService.existsById(1L)).thenReturn(true);
        when(movieService.updateMovie(eq(1L), org.mockito.ArgumentMatchers.any(Movie.class))).thenReturn(updatedMovie);


        mockMvc.perform(put("/movie/{id}", 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Inception 2\", \"genre\":\"Sci-Fi\", \"duration\":150, \"rating\":9.0, \"releaseYear\":2025}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Inception 2"))
                .andExpect(jsonPath("$.rating").value(9.0))
                .andExpect(jsonPath("$.releaseYear").value(2025));

        verify(movieService, times(1)).updateMovie(eq(1L), org.mockito.ArgumentMatchers.any(Movie.class));
    }

    @Test
    public void deleteMovie() throws Exception {
        doNothing().when(movieService).deleteMovie(eq(1L));

        mockMvc.perform(delete("/movie/{id}", 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void deletefMovieFailsWith403WhenSessionIsNotValid() throws Exception {
        doNothing().when(movieService).deleteMovieById(eq(1L));

        mockMvc.perform(delete("/movie/{id}", 1L))
                .andExpect(status().isForbidden());
    }

}
