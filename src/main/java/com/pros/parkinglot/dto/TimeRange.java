package com.pros.parkinglot.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TimeRange(@NotNull LocalDate checkIn, @NotNull LocalDate checkOut) {
}
