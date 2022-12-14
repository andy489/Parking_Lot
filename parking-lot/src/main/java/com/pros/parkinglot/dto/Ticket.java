package com.pros.parkinglot.dto;

import com.pros.parkinglot.model.slot.type.VehicleType;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class Ticket {
    @NonNull
    private final VehicleType vehicleType;
    private final String greetingMsg;
    private final String registrationNumber;
    @NonNull
    private final Integer slotId;
    @NonNull
    private final LocalDateTime parkedTime;



    public Ticket(@NonNull VehicleType vehicleType, String greetingMsg, String registrationNumber, @NonNull Integer slotId, @NonNull LocalDateTime parkedTime) {
        this.vehicleType = vehicleType;
        this.greetingMsg = greetingMsg;
        this.registrationNumber = registrationNumber;
        this.slotId = slotId;
        this.parkedTime = parkedTime;
    }
}

