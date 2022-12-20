package com.pros.parkinglot.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.pros.parkinglot.model.slot.type.VehicleType;

import lombok.NonNull;

public record VehicleDto(
        @NonNull VehicleType vehicleType,
        @JsonAlias(value = "regNum") String registrationNumber
) {
}
