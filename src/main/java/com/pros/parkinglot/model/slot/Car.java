package com.pros.parkinglot.model.slot;

import com.pros.parkinglot.model.slot.type.VehicleType;
import jakarta.persistence.Entity;

@Entity
public class Car extends Vehicle {
    public Car() {

    }

    public Car(String registrationNumber) {
        super(VehicleType.CAR, registrationNumber);
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.CAR;
    }
}
