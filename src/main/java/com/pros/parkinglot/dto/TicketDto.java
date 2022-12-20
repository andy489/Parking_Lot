package com.pros.parkinglot.dto;

import com.pros.parkinglot.model.slot.type.VehicleType;
import lombok.NonNull;

import java.time.LocalDateTime;

public record TicketDto(
        @NonNull VehicleType vehicleType,
        String greetingMsg, String registrationNumber,
        @NonNull Integer slotId, @NonNull LocalDateTime parkedTime
) { }

