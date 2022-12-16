package com.pros.parkinglot.mapper;

import com.pros.parkinglot.dto.ReportDto;
import com.pros.parkinglot.model.report.Report;
import com.pros.parkinglot.model.slot.type.VehicleType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class ReportDtoMapper {
    private static final int MINUTES_PER_HOUR = 60;
    private static final int HOURS_PER_DAY = 24;

    public double pricePerHourCar;
    public double pricePerDayCar;

    public double pricePerHourBus;
    public double pricePerDayBus;

    private final String delimiter;
    private final DateTimeFormatter customFormat;

    @Autowired
    public ReportDtoMapper(
            @Qualifier("getPricePerHourCar") double pricePerHourCar,
            @Qualifier("getPricePerDayCar") double pricePerDayCar,
            @Qualifier("getPricePerHourBus") double pricePerHourBus,
            @Qualifier("getPricePerDayBus") double pricePerDayBus,
            @Qualifier("basicDateFormatter") DateTimeFormatter customFormat,
            @Qualifier("commaDelimiter") String delimiter) {
        this.pricePerHourCar = pricePerHourCar;
        this.pricePerDayCar = pricePerDayCar;
        this.pricePerHourBus = pricePerHourBus;
        this.pricePerDayBus = pricePerDayBus;
        this.customFormat = customFormat;
        this.delimiter = delimiter;
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
            case CAR -> pricePerHourCar;
            case BUS -> pricePerHourBus;
            case OTHER -> 0.0;
        };

        double pricePerDay = switch (vehicleType) {
            case CAR -> pricePerDayCar;
            case BUS -> pricePerDayBus;
            case OTHER -> 0.0;
        };

        double hoursPay = (int) (minutes / MINUTES_PER_HOUR) * pricePerHour;
        double additionalStartedHourPay = (minutes % MINUTES_PER_HOUR) > 0 ? pricePerHour : 0.0;
        long numOfDays = (minutes / MINUTES_PER_HOUR) / HOURS_PER_DAY;
        double discountPerDay = numOfDays * (pricePerHour * HOURS_PER_DAY - pricePerDay);

        return BigDecimal.valueOf(Math.max(pricePerHour, hoursPay + additionalStartedHourPay - discountPerDay));
    }

    public String toCsvString(Report report) {
        String in = report.getCheckIn().format(customFormat);
        String out = report.getCheckOut().format(customFormat);

        return String.format(
                "%d%s%s%s%s%s%s%s%s%s%s%n",
                report.getReportId(), delimiter,
                report.getVehicleType(), delimiter,
                report.getRegistrationNumber(), delimiter,
                in, delimiter,
                out, delimiter,
                report.getPrice().toString()
        );
    }
}
