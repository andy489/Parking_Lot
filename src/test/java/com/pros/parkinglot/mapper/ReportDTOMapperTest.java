package com.pros.parkinglot.mapper;

import com.pros.parkinglot.dto.ReportDto;
import com.pros.parkinglot.model.report.Report;
import com.pros.parkinglot.model.slot.type.VehicleType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReportDTOMapperTest {
    ReportDtoMapper mapper = new ReportDtoMapper();

    @Test
    void testCalcPriceCarLessThanAMinute() {
        LocalDateTime in = LocalDateTime.now().minusSeconds(30);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = new ReportDto(
                in,
                out,
                "СА1234СВ",
                VehicleType.CAR
        );

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("1.00").compareTo(report.getPrice()), "Expected price for less than a minute to be as price per Hour for a Vehicle of type CAR");
    }

    @Test
    void testCalcPriceBusLessThanAMinute() {
        LocalDateTime in = LocalDateTime.now().minusSeconds(30);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDto = new ReportDto(
                in,
                out,
                "СА1234СВ",
                VehicleType.BUS
        );


        Report report = mapper.toReport(reportDto);

        assertEquals(0, new BigDecimal("5.00").compareTo(report.getPrice()), "Expected price for less than a minute to be as price per Hour for a Vehicle of type BUS");
    }

    @Test
    void testCalcPriceCarOneAndAHalfHoursStay() {
        LocalDateTime in = LocalDateTime.now().minusMinutes(90);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = new ReportDto(
                in,
                out,
                "СА1234СВ",
                VehicleType.CAR
        );

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("2.00").compareTo(report.getPrice()), "Expected price for CAR for 1 hour and a half to be same as price for 2 hours");
    }

    @Test
    void testCalcPriceBusOneAndAHalfHoursStay() {
        LocalDateTime in = LocalDateTime.now().minusMinutes(90);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = new ReportDto(
                in,
                out,
                "СА1234СВ",
                VehicleType.BUS
        );

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("10.00").compareTo(report.getPrice()), "Expected price for BUS for 1 hour and a half to be same as price for 2 hours");
    }

    @Test
    void testCalcPriceCarMoreThanADayStay() {
        LocalDateTime in = LocalDateTime.now().minusDays(1).minusHours(1).minusMinutes(30);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = new ReportDto(
                in,
                out,
                "СА1234СВ",
                VehicleType.CAR
        );

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("12.00").compareTo(report.getPrice()), "Expected price for CAR for more than a day to include discount");
    }

    @Test
    void testCalcPriceBusMoreThanADayStay() {
        LocalDateTime in = LocalDateTime.now().minusDays(1).minusHours(1).minusMinutes(30);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = new ReportDto(
                in,
                out,
                "СА1234СВ",
                VehicleType.BUS
        );

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("50.00").compareTo(report.getPrice()), "Expected price for BUS for more than a day to include discount");
    }

    @Test
    void testCalcPriceCarMoreThanThreeDaysStay() {
        LocalDateTime in = LocalDateTime.now().minusDays(3).minusHours(2).minusMinutes(25);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = new ReportDto(
                in,
                out,
                "СА1234СВ",
                VehicleType.CAR
        );

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("33.00").compareTo(report.getPrice()), "Expected price for BUS for more than 3 days to include discount");
    }

    @Test
    void testCalcPriceBusMoreThanThreeDaysStay() {
        LocalDateTime in = LocalDateTime.now().minusDays(3).minusHours(2).minusMinutes(25);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = new ReportDto(
                in,
                out,
                "СА1234СВ",
                VehicleType.BUS
        );

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("135.00").compareTo(report.getPrice()), "Expected price for BUS for more than 3 days to include discount");
    }
}
