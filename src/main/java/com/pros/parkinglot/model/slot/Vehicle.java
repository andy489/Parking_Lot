package com.pros.parkinglot.model.slot;

import com.pros.parkinglot.dto.VehicleDto;
import com.pros.parkinglot.model.slot.type.VehicleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.cfg.NotYetImplementedException;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name="current_vehicles")
public abstract class Vehicle implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static final String EMPTY_REG_NUM = "";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer slotId = 123;

    @Column(name = "reg_num")
    private String registrationNumber;

    @Column(name = "check_in")
    private LocalDateTime checkIn;

    public static Vehicle of(VehicleDto vehicleDTO) {
        return switch (vehicleDTO.getVehicleType()) {
            case CAR -> new Car(vehicleDTO.getRegistrationNumber());
            case BUS -> new Bus(vehicleDTO.getRegistrationNumber());
            case OTHER -> throw new NotYetImplementedException("Only CAR and BUS vehicles allowed at this point!");
        };
    }

    public Vehicle() {
    }

    public Vehicle(String registrationNumber) {
        this.registrationNumber = registrationNumber;
        this.checkIn = LocalDateTime.now();
    }

    public Vehicle(LocalDateTime checkIn,  VehicleType vehicleType, String registrationNumber) {
        this.registrationNumber = registrationNumber;
        this.checkIn = checkIn;
    }

    public void nullParkedTime() {
        checkIn = null;
    }

    public void resetParkedTime() {
        checkIn = LocalDateTime.now();
    }

    public abstract VehicleType getVehicleType();
}
