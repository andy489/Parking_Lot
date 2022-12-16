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

    @Override
    public String toString() {
        return String.format("Vehicle(%d, %s, %s, %s-%s-%s %s:%s)%n",
                this.getSlotId(),
                this.getVehicleType(),
                this.getRegistrationNumber(),
                this.getCheckIn().getYear(), this.getCheckIn().getDayOfMonth(), this.getCheckIn().getDayOfMonth(),
                this.getCheckIn().getHour(), this.getCheckIn().getMinute()
        );
    }
}
