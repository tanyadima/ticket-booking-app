package com.booking_app.booking_app.controller;

import com.booking_app.booking_app.dto.ShowtimeRequest;
import com.booking_app.booking_app.model.Movie;
import com.booking_app.booking_app.model.Showtime;
import com.booking_app.booking_app.security.JwtUtil;
import com.booking_app.booking_app.service.CustomUserDetailsService;
import com.booking_app.booking_app.service.MovieService;
import com.booking_app.booking_app.service.ShowtimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({ShowtimeController.class})
public class ShowtimeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @MockBean
    private ShowtimeService showtimeService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    private String token;

    private final Movie movie = new Movie(1L, "Movie A", "Action", 120, 8.5, 2023);
    private Showtime getShowtime1(){
        Showtime showtime1 = new Showtime();
        showtime1.setId(1L);
        showtime1.setMovie(movie);
        showtime1.setTheater("Theater A");
        showtime1.setStartTime(LocalDateTime.now());
        showtime1.setEndTime(LocalDateTime.now().plusHours(2));
        return showtime1;
    };
    private Showtime getShowtime2(){
        Showtime showtime2 = new Showtime();
        showtime2.setId(2L);
        showtime2.setMovie(movie);
        showtime2.setTheater("Theater B");
        showtime2.setStartTime(LocalDateTime.now().plusHours(3));
        showtime2.setEndTime(LocalDateTime.now().plusHours(5));
        return showtime2;
    };

    @BeforeEach
    public void init() {
        // Generate a new token before each test
        token = jwtUtil.generateToken("admin", "ROLE_ADMIN");
    }

    @Test
    public void testGetShowtimesByMovie() throws Exception {
        Showtime showtime1 = this.getShowtime1();
        Showtime showtime2 = this.getShowtime2();
        when(showtimeService.getShowtimesByMovie("Movie A")).thenReturn(Arrays.asList(showtime1, showtime2));

        mockMvc.perform(get("/showtime/movie?movieTitle=Movie A")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].theater", is("Theater A")))
                .andExpect(jsonPath("$[1].theater", is("Theater B")));
    }

    @Test
    public void testGetShowtimesByTheater() throws Exception {
        Showtime showtime1 = this.getShowtime1();
        when(showtimeService.getShowtimesByTheater("Theater A")).thenReturn(Arrays.asList(showtime1));

        mockMvc.perform(get("/showtime/theater?theater=Theater A")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].theater", is("Theater A")));
    }


    @Test
    public void testGetAllShowtimes() throws Exception {
        Showtime showtime1 = this.getShowtime1();
        Showtime showtime2 = this.getShowtime2();

        when(showtimeService.getAllShowtimes()).thenReturn(Arrays.asList(showtime1, showtime2));

        mockMvc.perform(get("/showtime")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].theater", is("Theater A")))
                .andExpect(jsonPath("$[1].theater", is("Theater B")));
    }

    @Test
    public void deleteShowtime() throws Exception {
        doNothing().when(showtimeService).deleteShowtimeById(eq(1L));

        mockMvc.perform(delete("/showtime/{id}", 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .header("Authorization", "Bearer " + token)
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddShowtime() throws Exception {
        Showtime showtime = this.getShowtime1();
        showtime.setStartTime(LocalDateTime.parse("2025-01-19T10:00:00"));
        showtime.setEndTime(LocalDateTime.parse("2025-01-19T12:00:00"));

        when(showtimeService.addShowtime(Mockito.any(ShowtimeRequest.class))).thenReturn(showtime);

        mockMvc.perform(post("/showtime")
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .header("Authorization", "Bearer " + token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{" +
                                "\"movie\": \"Movie A\"," +
                                "\"theater\": \"Theater A\"," +
                                "\"startTime\": \"2025-01-19T10:00:00\"," +
                                "\"endTime\": \"2025-01-19T12:00:00\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.theater", is("Theater A")));
    }

    @Test
    public void updateShowtime() throws Exception {
        Showtime updatedShowtime = this.getShowtime1();
        updatedShowtime.setTheater("updatedTheater");

        when(showtimeService.updateShowtime(eq(1L), org.mockito.ArgumentMatchers.any(ShowtimeRequest.class))).thenReturn(updatedShowtime);

        mockMvc.perform(put("/showtime/{id}", 1L)
                        .with(SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN"))
                        .header("Authorization", "Bearer " + token)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"movie\":\"Movie 1\", \"theater\":\"Theater A\", \"startTime\":\"2025-01-18T14:00:00\", \"endTime\":\"2025-01-18T16:00:00\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.theater").value("Theater A"));

        verify(showtimeService, times(1)).updateShowtime(eq(1L), org.mockito.ArgumentMatchers.any(ShowtimeRequest.class));
    }

    @Test
    public void deleteShowtimeFailsWith403WhenSessionIsNotValid() throws Exception {
        doNothing().when(showtimeService).deleteShowtimeById(eq(1L));

        mockMvc.perform(delete("/showtime/{id}", 1L))
                .andExpect(status().isForbidden());
    }

}

