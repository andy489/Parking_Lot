package com.pros.parkinglot.controller;

import com.pros.parkinglot.dto.VehicleDTO;
import com.pros.parkinglot.dto.Ticket;
import com.pros.parkinglot.model.slot.Vehicle;
import com.pros.parkinglot.model.slot.type.VehicleType;
import com.pros.parkinglot.service.SlotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = {"/api"})
public class SlotController {

    private final SlotService slotService;

    @Autowired
    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping(value = {"/template"})
    public Vehicle templateCheat() {
        return new Vehicle(VehicleType.OTHER, "CВ1234PB");
    }

    @GetMapping(value = {"/parking"})
    public ResponseEntity<List<Vehicle>> getCurrentParkingState(){
        return new ResponseEntity<>(slotService.getCurrentParkingState(), HttpStatus.OK);
    }

    @PostMapping(value = {"/car"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Ticket> parkSingle(@RequestBody @Valid VehicleDTO vehicleDTO) {
        return new ResponseEntity<>(slotService.checkIn(vehicleDTO), HttpStatus.OK);
    }

    @Transactional
    @PostMapping(value = {"/cars"}, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Ticket>> parkMultiple(@RequestBody VehicleDTO... vehicleDTOs) {
        return new ResponseEntity<>(slotService.checkIn(vehicleDTOs), HttpStatus.OK);
    }
}
