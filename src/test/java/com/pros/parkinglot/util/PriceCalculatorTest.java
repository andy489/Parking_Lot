package com.pros.parkinglot.util;

import com.pros.parkinglot.mapper.ReportDtoMapper;
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

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class PriceCalculatorTest {
    @Value("${parking.lot.price.per.hour.car}")
    private double pricePerHourCar;
    @Value("${parking.lot.price.per.day.car}")
    private double pricePerDayCar;

    @Value("${parking.lot.price.per.hour.bus}")
    private double pricePerHourBus;
    @Value("${parking.lot.price.per.day.bus}")
    private double pricePerDayBus;

    private ReportDtoMapper mapper;
    private PriceCalculator priceCalculator;

    @BeforeEach
    public void setUp() {
        priceCalculator = new PriceCalculator(pricePerHourCar, pricePerDayCar, pricePerHourBus, pricePerDayBus);
        mapper = new ReportDtoMapper(priceCalculator);
    }

    @Test
    public void testCalcPriceCarLessThanAMinute() {
        LocalDateTime in = LocalDateTime.now().minusSeconds(30);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = ReportDto.of(in, out, "СА1234СВ", VehicleType.CAR);

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("1.00").compareTo(report.getPrice()),
                "Expected price for less than a minute to be as price per Hour for a Vehicle of type CAR");
    }

    @Test
    public void testCalcPriceBusLessThanAMinute() {
        LocalDateTime in = LocalDateTime.now().minusSeconds(30);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDto = ReportDto.of(in, out, "СА1234СВ", VehicleType.BUS);

        Report report = mapper.toReport(reportDto);

        assertEquals(0, new BigDecimal("5.00").compareTo(report.getPrice()),
                "Expected price for less than a minute to be as price per Hour for a Vehicle of type BUS");
    }

    @Test
    public void testCalcPriceCarTwoAndAHalfHoursStay() {
        LocalDateTime in = LocalDateTime.now().minusMinutes(150);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = ReportDto.of(in, out, "СА1234СВ", VehicleType.CAR);

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("3.00").compareTo(report.getPrice()),
                "Expected price for CAR for 2 and a half hours to be same as price for 3 hours");
    }

    @Test
    public void testCalcPriceBusTwoAndAHalfHoursStay() {
        LocalDateTime in = LocalDateTime.now().minusMinutes(150);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = ReportDto.of(in, out, "СА1234СВ", VehicleType.BUS);

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("15.00").compareTo(report.getPrice()),
                "Expected price for BUS for 2 and a half hours to be same as price for 3 hours");
    }

    @Test
    public void testCalcPriceCarMoreThanADayStayButLessThanTwoDaysTax() {
        LocalDateTime in = LocalDateTime.now().minusDays(1).minusHours(1).minusMinutes(30);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = ReportDto.of(in, out, "СА1234СВ", VehicleType.CAR);

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("12.00").compareTo(report.getPrice()),
                "Expected price for CAR for 1 day and 1 and a half hours to consider exactly 1 day payment tax and the remainder to be taxed as per hour");
    }

    @Test
    public void testCalcPriceBusMoreThanADayStayButLessThanTwoDaysTax() {
        LocalDateTime in = LocalDateTime.now().minusDays(1).minusHours(1).minusMinutes(30);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = ReportDto.of(in, out, "СА1234СВ", VehicleType.BUS);

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("50.00").compareTo(report.getPrice()),
                "Expected price for BUS for 1 day and 1 and a half hours to consider exactly 1 day payment tax and the remainder to be taxed as per hour");
    }

    @Test
    public void testCalcPriceCarMoreThanThreeDaysStayButLessThanFourDaysTax() {
        LocalDateTime in = LocalDateTime.now().minusDays(3).minusHours(2).minusMinutes(25);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = ReportDto.of(in, out, "СА1234СВ", VehicleType.CAR);

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("33.00").compareTo(report.getPrice()),
                "Expected price for CAR for 3 day, 2 hours and 25 minutes to consider exactly 3 day payment tax and the remainder to be taxed as per hour");
    }

    @Test
    public void testCalcPriceBusMoreThanThreeDaysStayButLessThanFourDaysTax() {
        LocalDateTime in = LocalDateTime.now().minusDays(3).minusHours(2).minusMinutes(25);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = ReportDto.of(in, out, "СА1234СВ", VehicleType.BUS);

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("135.00").compareTo(report.getPrice()),
                "Expected price for BUS for 3 day, 2 hours and 25 minutes to consider exactly 3 day payment tax and the remainder to be taxed as per hour");
    }

    @Test
    public void testCalcPriceCarMoreThanOneDayButLessThanTwoDaysStayAndTwoDaysTaxIsCheaper() {
        LocalDateTime in = LocalDateTime.now().minusDays(1).minusHours(10 + 2);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = ReportDto.of(in, out, "СА1234СВ", VehicleType.CAR);

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("20.00").compareTo(report.getPrice()),
                "Expected price for CAR for 1 day and 12 hours to consider exactly 2 day payment tax");
    }

    @Test
    public void testCalcPriceBusMoreThanOneDayButLessThanTwoDaysStayAndTwoDaysTaxIsCheaper() {
        LocalDateTime in = LocalDateTime.now().minusDays(1).minusHours(8 + 2);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = ReportDto.of(in, out, "СА1234СВ", VehicleType.BUS);

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("80.00").compareTo(report.getPrice()),
                "Expected price for BUS for 1 day and 12 hours to consider exactly 2 day payment tax");
    }

    @Test
    public void testCalcPriceCarMoreThanOneDayButLessThanTwoDaysStayAndTwoDaysTaxIsNotCheaper() {
        LocalDateTime in = LocalDateTime.now().minusDays(1).minusHours(10 - 1);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = ReportDto.of(in, out, "СА1234СВ", VehicleType.CAR);

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("19.00").compareTo(report.getPrice()),
                "Expected price for CAR for 1 day and 9 hours to consider exactly 1 day payment tax and the remainder to be taxed as per hour ");
    }

    @Test
    public void testCalcPriceBusMoreThanOneDayButLessThanTwoDaysStayAndTwoDaysTaxIsNotCheaper() {
        LocalDateTime in = LocalDateTime.now().minusDays(1).minusHours(8 - 1);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = ReportDto.of(in, out, "СА1234СВ", VehicleType.BUS);

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("75.00").compareTo(report.getPrice()),
                "Expected price for BUS for 1 day and 9 hours to consider exactly 1 day payment tax and the remainder to be taxed as per hour ");
    }

    @Test
    public void testCalcPriceCarMoreThanOneDayButLessThanTwoDaysStayAndTwoDaysTaxIsCheaperBecauseOfStartedHour() {
        LocalDateTime in = LocalDateTime.now().minusDays(1).minusHours(9).minusMinutes(5);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = ReportDto.of(in, out, "СА1234СВ", VehicleType.CAR);

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("20.00").compareTo(report.getPrice()),
                "Expected price for CAR for 1 day and 9 hours to consider exactly 1 day payment tax and the remainder to be taxed as per hour ");
    }

    @Test
    public void testCalcPriceBusMoreThanOneDayButLessThanTwoDaysStayAndTwoDaysTaxIsNotCheaperBecauseOfStartedHour() {
        LocalDateTime in = LocalDateTime.now().minusDays(1).minusHours(7).minusMinutes(5);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = ReportDto.of(in, out, "СА1234СВ", VehicleType.BUS);

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("80.00").compareTo(report.getPrice()),
                "Expected price for BUS for 1 day and 9 hours to consider exactly 1 day payment tax and the remainder to be taxed as per hour ");
    }

    @Test
    public void testCalcPriceCarMoreThanOneDayAndLessThanTwoDaysStayButTwoDaysTaxIsNotCheaperBecauseOfLackOfMoreThanAHour() {
        LocalDateTime in = LocalDateTime.now().minusDays(1).minusHours(9).plusMinutes(10);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = ReportDto.of(in, out, "СА1234СВ", VehicleType.CAR);

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("19.00").compareTo(report.getPrice()),
                "Expected price for CAR for 1 day, 8 hours and 50 minutes to consider exactly 1 day payment tax and the remainder to be taxed as per hour ");
    }

    @Test
    public void testCalcPriceBusMoreThanOneDayAndLessThanTwoDaysStayButTwoDaysTaxIsNotCheaperBecauseOfLackOfMoreThanAHour() {
        LocalDateTime in = LocalDateTime.now().minusDays(1).minusHours(7).plusMinutes(10);
        LocalDateTime out = LocalDateTime.now();

        ReportDto reportDTO = ReportDto.of(in, out, "СА1234СВ", VehicleType.BUS);

        Report report = mapper.toReport(reportDTO);

        assertEquals(0, new BigDecimal("75.00").compareTo(report.getPrice()),
                "Expected price for BUS for 1 day, 8 hours and 50 minutes to consider exactly 1 day payment tax and the remainder to be taxed as per hour ");
    }
}
