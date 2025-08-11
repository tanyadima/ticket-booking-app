package com.booking_app.booking_app.dto;

import java.math.BigDecimal;

public record BookingRequest(String userName, Long showtimeId, String movieTitle, Integer seatNumber, BigDecimal price ) {
    public String getUserName() {
        return userName;
    }

    public Long getShowtimeId() {
        return showtimeId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public BigDecimal getPrice() {
        return price;
    }
}

