package com.pros.parkinglot.mapper;

import com.pros.parkinglot.dto.ReportDTO;
import com.pros.parkinglot.model.report.Report;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Component
public class ReportDTOMapper {
    @Value("${parking.lot.price.per.hour.car}")
    private int CAR_PRICE_PER_HOUR;
    @Value("${parking.lot.price.per.hour.bus}")
    private int BUS_PRICE_PER_HOUR;

    @Value("${parking.lot.price.per.day.car}")
    private int CAR_PRICE_PER_DAY;
    @Value("${parking.lot.price.per.day.bus}")
    private int BUS_PRICE_PER_DAY;

    private static final int HOURS_PER_DAY = 24;
    private static final int MINUTES_PER_HOUR = 60;

    public Report toReport(ReportDTO reportDTO){
        BigDecimal bigDecimal = calcPrice(ChronoUnit.MINUTES.between(reportDTO.getCheckIn(), reportDTO.getCheckOut()));

        return new Report(
                reportDTO.getCheckIn(),
                reportDTO.getCheckOut(),
                reportDTO.getRegistrationNumber(),
                reportDTO.getVehicleType(),
                bigDecimal
        );
    }

    private BigDecimal calcPrice(long minutes) {
        double hoursPay = (minutes * 1.0 / MINUTES_PER_HOUR) * CAR_PRICE_PER_HOUR;
        double additionalStartedHourPay = (minutes % MINUTES_PER_HOUR) > 0 ? CAR_PRICE_PER_DAY : 0.0;
        long numOfDays = (minutes / MINUTES_PER_HOUR) / HOURS_PER_DAY;
        double discountPerDay = hoursPay - numOfDays * CAR_PRICE_PER_DAY;
        ////////////////////////////////////////////////////////// Bus pay? TODO
        return new BigDecimal(Math.max(CAR_PRICE_PER_HOUR,hoursPay + additionalStartedHourPay - discountPerDay));
    }

    public ReportDTO toReportDTO(Report report){
        return new ReportDTO(
                report.getCheckIn(),
                report.getCheckOut(),
                report.getRegistrationNumber(),
                report.getVehicleType(),
                report.getPrice()
        );
    }
}
