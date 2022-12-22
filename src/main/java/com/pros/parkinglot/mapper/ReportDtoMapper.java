package com.pros.parkinglot.mapper;

import com.pros.parkinglot.dto.ReportDto;
import com.pros.parkinglot.model.report.Report;
import com.pros.parkinglot.model.slot.type.VehicleType;

import com.pros.parkinglot.util.PriceCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class ReportDtoMapper {

    private final PriceCalculator priceCalculator;

    @Autowired
    public ReportDtoMapper(PriceCalculator priceCalculator){
        this.priceCalculator=priceCalculator;
    }

    public ReportDto toReportDTO(Report report) {
        return new ReportDto(
                report.getCheckIn(),
                report.getCheckOut(),
                report.getRegistrationNumber(),
                report.getVehicleType(),
                report.getPrice()
        );
    }

    public Report toReport(ReportDto reportDTO) {
        LocalDateTime checkIn = reportDTO.checkIn();
        LocalDateTime checkOut = reportDTO.checkOut();
        VehicleType vehicleType = reportDTO.vehicleType();

        BigDecimal price = priceCalculator.calcPrice(ChronoUnit.MINUTES.between(checkIn, checkOut), vehicleType);

        return new Report(
                checkIn,
                checkOut,
                reportDTO.registrationNumber(),
                vehicleType,
                price
        );
    }
}
