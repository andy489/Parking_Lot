package com.pros.parkinglot.controller;

import com.pros.parkinglot.dto.ReportDto;
import com.pros.parkinglot.dto.VehicleDto;
import com.pros.parkinglot.dto.TicketDto;
import com.pros.parkinglot.model.slot.Car;
import com.pros.parkinglot.model.slot.Vehicle;
import com.pros.parkinglot.service.ParkingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping (value = {"/api"})
public class ParkingController {

    private final ParkingService slotService;

    @Autowired
    public ParkingController(ParkingService slotService) {
        this.slotService = slotService;
    }

    @GetMapping(value = {"/template/slot"})
    public ResponseEntity<Vehicle> templateCheat() {
        return new ResponseEntity<>(new Car("CВ1234PB"), HttpStatus.OK);
    }


    @GetMapping(value = {"/parking"})
    public ResponseEntity<List<Vehicle>> getCurrentParkingState() {
        return new ResponseEntity<>(slotService.getCurrentParkingState(), HttpStatus.OK);
    }

    @Transactional
    @PostMapping(value = {"/vehicle"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TicketDto> parkSingle(@RequestBody @Valid VehicleDto vehicleDTO) {
        return new ResponseEntity<>(slotService.checkIn(vehicleDTO), HttpStatus.OK);
    }

    @Transactional
    @PostMapping(value = {"/vehicles"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TicketDto>> parkMultiple(@RequestBody VehicleDto... vehicleDtos) {
        return new ResponseEntity<>(slotService.checkIn(vehicleDtos), HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("/{id}")
    public ResponseEntity<ReportDto> checkOutVehicle(@PathVariable("id") Integer slotId) {
        return new ResponseEntity<>(slotService.checkOut(slotId), HttpStatus.OK);
    }
}
