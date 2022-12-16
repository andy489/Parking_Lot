package com.pros.parkinglot.mapper;

import com.pros.parkinglot.dto.ReportDto;
import com.pros.parkinglot.model.report.Report;
import com.pros.parkinglot.model.slot.type.VehicleType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class ReportDtoMapper {
    private static final int MINUTES_PER_HOUR = 60;
    private static final int HOURS_PER_DAY = 24;

    //    @Value("${parking.lot.price.per.hour.car}")
    private static Integer PRICE_PER_HOUR_CAR = 1;
    //    @Value("${parking.lot.price.per.day.car}")
    private Integer PRICE_PER_DAY_CAR = 10;

    //    @Value("${parking.lot.price.per.hour.bus}")
    private Integer PRICE_PER_HOUR_BUS = 5;
    //    @Value("${parking.lot.price.per.day.bus}")
    private Integer PRICE_PER_DAY_BUS = 40;

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
        LocalDateTime checkIn = reportDTO.getCheckIn();
        LocalDateTime checkOut = reportDTO.getCheckOut();
        VehicleType vehicleType = reportDTO.getVehicleType();

        BigDecimal price = calcPrice(ChronoUnit.MINUTES.between(checkIn, checkOut), vehicleType);

        return new Report(
                checkIn,
                checkOut,
                reportDTO.getRegistrationNumber(),
                vehicleType,
                price
        );
    }

    private BigDecimal calcPrice(long minutes, VehicleType vehicleType) {
        double pricePerHour = switch (vehicleType) {
            case CAR -> PRICE_PER_HOUR_CAR;
            case BUS -> PRICE_PER_HOUR_BUS;
            case OTHER -> 0.0;
        };

        double pricePerDay = switch (vehicleType) {
            case CAR -> PRICE_PER_DAY_CAR;
            case BUS -> PRICE_PER_DAY_BUS;
            case OTHER -> 0.0;
        };

        double hoursPay = (int) (minutes / MINUTES_PER_HOUR) * pricePerHour;
        double additionalStartedHourPay = (minutes % MINUTES_PER_HOUR) > 0 ? pricePerHour : 0.0;
        long numOfDays = (minutes / MINUTES_PER_HOUR) / HOURS_PER_DAY;
        double discountPerDay = numOfDays * (pricePerHour * HOURS_PER_DAY - pricePerDay);

        return BigDecimal.valueOf(Math.max(pricePerHour, hoursPay + additionalStartedHourPay - discountPerDay));
    }
}
