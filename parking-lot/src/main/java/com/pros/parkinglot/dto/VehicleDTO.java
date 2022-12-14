package com.pros.parkinglot.dto;

import com.pros.parkinglot.model.slot.type.VehicleType;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public class VehicleDTO {
    @NonNull
    private final VehicleType vehicleType;
    private final String registrationNumber;

    public VehicleDTO(VehicleType vehicleType, String registrationNumber) {
        this.vehicleType = vehicleType;
        this.registrationNumber = registrationNumber;
    }
}
