package com.pros.parkinglot;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class ParkingLotApplication {
    public static void main(String[] args) {
        SpringApplication.run(ParkingLotApplication.class, args);
    }
}
