package com.pros.parkinglot.dto;

import com.pros.parkinglot.model.slot.type.VehicleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@ToString
public class ReportDTO {
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String registrationNumber;
    private VehicleType vehicleType;
    private BigDecimal price = new BigDecimal("0.0");

    public ReportDTO() {
    }

    public ReportDTO(
            LocalDateTime checkIn,
            LocalDateTime checkOut,
            String registrationNumber,
            VehicleType vehicleType
    ) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
    }

    public ReportDTO(
            LocalDateTime checkIn,
            LocalDateTime checkOut,
            String registrationNumber,
            VehicleType vehicleType,
            BigDecimal price
    ) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
        this.price = price;
    }
}
