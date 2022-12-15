package com.pros.parkinglot.dto;

import com.pros.parkinglot.model.slot.type.VehicleType;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@ToString
public class ReportDto {
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String registrationNumber;
    private VehicleType vehicleType;
    private BigDecimal price;

    public ReportDto() {
    }

    public ReportDto(
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

    public ReportDto(
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
