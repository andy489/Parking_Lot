package com.pros.parkinglot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pros.parkinglot.model.slot.type.VehicleType;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public class VehicleDto {
    @NonNull
    private final VehicleType vehicleType;
    @JsonProperty(value = "regNum")
    private final String registrationNumber;

    public VehicleDto(VehicleType vehicleType, String registrationNumber) {
        this.vehicleType = vehicleType;
        this.registrationNumber = registrationNumber;
    }
}
