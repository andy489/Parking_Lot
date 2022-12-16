package com.pros.parkinglot.model.slot;

import com.pros.parkinglot.model.slot.type.VehicleType;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class Bus extends Vehicle {
    public Bus() {

    }

    public Bus(String registrationNumber) {
        super(registrationNumber);
    }

    public Bus(LocalDateTime checkIn, String registrationNumber) {
        super(checkIn, VehicleType.CAR, registrationNumber);
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.BUS;
    }
}
