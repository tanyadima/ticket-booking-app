package com.booking_app.booking_app.requests;

import java.time.LocalDateTime;

public class ShowtimeRequest {
    private String movie;
    private String theater;
    private LocalDateTime startTime;
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovieTitle(String movieTitle) {
        this.movie = movieTitle;
    }

    public String getTheater() {
        return theater;
    }

    public void setTheater(String theater) {
        this.theater = theater;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    private LocalDateTime endTime;

}

