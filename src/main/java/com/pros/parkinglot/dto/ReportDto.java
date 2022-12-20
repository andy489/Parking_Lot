package com.pros.parkinglot.dto;

import com.pros.parkinglot.model.slot.type.VehicleType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReportDto(
        LocalDateTime checkIn,
        LocalDateTime checkOut,
        String registrationNumber,
        VehicleType vehicleType,
        BigDecimal price) {

    public static ReportDto of(
            LocalDateTime checkIn,
            LocalDateTime checkOut,
            String registrationNumber,
            VehicleType vehicleType
    ) {
        return new ReportDto(checkIn, checkOut, registrationNumber, vehicleType, new BigDecimal(-1));
    }

    public static ReportDto of(
            LocalDateTime checkIn,
            LocalDateTime checkOut,
            String registrationNumber,
            VehicleType vehicleType,
            BigDecimal price
    ) {
        return new ReportDto(checkIn, checkOut, registrationNumber, vehicleType, price);
    }
}
