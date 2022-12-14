package com.pros.parkinglot.mapper;

import com.pros.parkinglot.dto.ReportDTO;
import com.pros.parkinglot.dto.VehicleDTO;
import com.pros.parkinglot.dto.Ticket;
import com.pros.parkinglot.model.report.Report;
import com.pros.parkinglot.model.slot.Vehicle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Component
public class SlotDtoMapper {
    @Value("${parking.lot.greeting.msg}")
    private String GREETING_MSG;

    public Vehicle toVehicle(VehicleDTO vehicleDTO) {
        return new Vehicle(vehicleDTO.getVehicleType(), vehicleDTO.getRegistrationNumber());
    }

    public VehicleDTO slotDTO(Vehicle slot) {
        return new VehicleDTO(slot.getVehicleType(), slot.getRegistrationNumber());
    }

    public Ticket toTicket(Vehicle slot) {
        return new Ticket(slot.getVehicleType(), GREETING_MSG, slot.getRegistrationNumber(), slot.getSlotId(), slot.getParkedTime());
    }
}
