package com.pros.parkinglot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.format.DateTimeFormatter;

@Configuration
public class ParkingLotConfiguration {
    @Value("${parking.lot.reports.file.date.format}")
    private String customDateFormat;

    @Value("${parking.lot.reports.file.delimiter}")
    private String commaDelimiter;

    @Value("${parking.lot.car.slots}")
    private int carSlots;
    @Value("${parking.lot.bus.slots}")
    private int busSlots;

    @Value("${parking.lot.price.per.hour.car}")
    private double pricePerHourCar;
    @Value("${parking.lot.price.per.day.car}")
    private double pricePerDayCar;

    @Value("${parking.lot.price.per.hour.bus}")
    private double pricePerHourBus;
    @Value("${parking.lot.price.per.day.bus}")
    private double pricePerDayBus;

    @Bean
    public DateTimeFormatter basicDateFormatter() {
        return DateTimeFormatter.ofPattern(customDateFormat);
    }

    @Bean
    public String commaDelimiter() {
        return commaDelimiter;
    }

    @Bean
    public int getCarSlots(){
        return carSlots;
    }

    @Bean
    public int getBusSlots(){
        return busSlots;
    }

    @Bean
    public double getPricePerHourCar(){
        return pricePerHourCar;
    }

    @Bean
    public double getPricePerDayCar(){
        return pricePerDayCar;
    }

    @Bean
    public double getPricePerHourBus(){
        return pricePerHourBus;
    }

    @Bean
    public double getPricePerDayBus(){
        return pricePerDayBus;
    }
}
