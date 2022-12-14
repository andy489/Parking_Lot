package com.pros.parkinglot.model.slot;

import com.pros.parkinglot.model.slot.type.VehicleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "slots")
public class Vehicle implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private static String EMPTY_REG_NUM = "";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer slotId = 123;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Column(name = "reg_num")
    private String registrationNumber;

    @Column(name = "check_in")
    private LocalDateTime parkedTime;

    public Vehicle() {
    }

    public Vehicle(VehicleType vehicleType, String registrationNumber) {
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
        this.parkedTime = LocalDateTime.now();
    }

    public Vehicle(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
        this.registrationNumber = EMPTY_REG_NUM;
        this.parkedTime = LocalDateTime.now();
    }
}
