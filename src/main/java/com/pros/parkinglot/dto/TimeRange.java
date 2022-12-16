package com.pros.parkinglot.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TimeRange {
    @NotNull
    private final LocalDate checkIn;
    @NotNull
    private final LocalDate checkOut;

    public TimeRange(LocalDate checkIn, LocalDate checkOut) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
    }
}
