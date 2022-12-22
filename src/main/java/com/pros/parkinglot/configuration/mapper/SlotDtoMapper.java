package com.pros.parkinglot.configuration.mapper;

import com.pros.parkinglot.dto.VehicleDto;
import com.pros.parkinglot.dto.TicketDto;
import com.pros.parkinglot.model.slot.Vehicle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SlotDtoMapper {
    @Value("${parking.lot.greeting.msg}")
    private String GREETING_MSG;

    public Vehicle toVehicle(VehicleDto vehicleDTO) {
        return Vehicle.of(vehicleDTO);
    }

    public TicketDto toTicket(Vehicle slot) {
        return new TicketDto(
                slot.getVehicleType(),
                GREETING_MSG,
                slot.getRegistrationNumber(),
                slot.getSlotId(),
                slot.getCheckIn()
        );
    }
}
