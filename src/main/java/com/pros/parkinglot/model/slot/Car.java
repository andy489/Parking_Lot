package com.pros.parkinglot.model.slot;

import com.pros.parkinglot.model.slot.type.VehicleType;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class Car extends Vehicle {
    public Car() {

    }

    public Car(String registrationNumber) {
        super(registrationNumber);
    }

    public Car(LocalDateTime checkIn, String registrationNumber) {
        super(checkIn, VehicleType.CAR, registrationNumber);
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.CAR;
    }
}
