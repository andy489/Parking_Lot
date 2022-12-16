package com.pros.parkinglot.mapper;

import com.pros.parkinglot.dto.ReportDto;
import com.pros.parkinglot.model.report.Report;
import com.pros.parkinglot.model.slot.type.VehicleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class ReportDTOMapperTest {

    @Value("${parking.lot.reports.file.delimiter}")
    private String commaDelimiter;
    @Value("${parking.lot.reports.file.date.format}")
    private String customDateFormat;

    @Value("${parking.lot.price.per.hour.car}")
    private double pricePerHourCar;
    @Value("${parking.lot.price.per.day.car}")
    private double pricePerDayCar;

    @Value("${parking.lot.price.per.hour.bus}")
    private double pricePerHourBus;
    @Value("${parking.lot.price.per.day.bus}")
    private double pricePerDayBus;

    private ReportDtoMapper mapper;

    @BeforeEach
    public void setUp() {
        mapper = new ReportDtoMapper(
                pricePerHourCar,
                pricePerDayCar,
                pricePerHourBus,
                pricePerDayBus,
                DateTimeFormatter.ofPattern(customDateFormat),
                commaDelimiter
        );
    }

    @Test
    public void testCalcPriceCarLessThanAMinute() {
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
    public void testCalcPriceBusLessThanAMinute() {
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
    public void testCalcPriceCarOneAndAHalfHoursStay() {
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
    public void testCalcPriceBusOneAndAHalfHoursStay() {
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
    public void testCalcPriceCarMoreThanADayStay() {
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
    public void testCalcPriceBusMoreThanADayStay() {
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
    public void testCalcPriceCarMoreThanThreeDaysStay() {
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
    public void testCalcPriceBusMoreThanThreeDaysStay() {
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
