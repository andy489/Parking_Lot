package com.pros.parkinglot.model.slot;

import com.pros.parkinglot.model.slot.type.VehicleType;

import jakarta.persistence.Entity;

@Entity
public class Bus extends Vehicle {
    public Bus() {

    }

    public Bus(String registrationNumber) {
        super(VehicleType.BUS, registrationNumber);
    }

    @Override
    public VehicleType getVehicleType() {
        return VehicleType.BUS;
    }
}
