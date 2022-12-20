package com.pros.parkinglot.service;

import com.pros.parkinglot.dto.ReportDto;
import com.pros.parkinglot.dto.VehicleDto;
import com.pros.parkinglot.dto.TicketDto;
import com.pros.parkinglot.exception.DuplicateRegistrationNumberException;
import com.pros.parkinglot.exception.NoAvailableSlotsException;
import com.pros.parkinglot.exception.NotAvailableVehicleInTheParkingException;
import com.pros.parkinglot.mapper.ReportDtoMapper;
import com.pros.parkinglot.mapper.SlotDtoMapper;
import com.pros.parkinglot.model.slot.Vehicle;
import com.pros.parkinglot.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ParkingService {

    private final int carSlots;
    private final int busSlots;

    private final VehicleRepository vehicleRepo;
    private final ReportService reportService;
    private final SlotDtoMapper slotDtoMapper;
    private final ReportDtoMapper reportDtoMapper;

    @Autowired
    public ParkingService(
            VehicleRepository vehicleRepo,
            ReportService reportService,
            SlotDtoMapper slotDtoMapper,
            ReportDtoMapper reportDtoMapper,
            @Qualifier("getCarSlots")
            int carSlots,
            @Qualifier("getBusSlots")
            int busSlots
    ) {
        this.vehicleRepo = vehicleRepo;
        this.reportService = reportService;
        this.slotDtoMapper = slotDtoMapper;
        this.reportDtoMapper = reportDtoMapper;

        this.carSlots=carSlots;
        this.busSlots=busSlots;
    }

    public List<Vehicle> getCurrentParkingState() {
        return getAllVehicles().stream().filter(isTaken()).collect(Collectors.toList());
    }

    public TicketDto checkInSingle(VehicleDto vehicleDto) {
        if (vehicleDto == null) {
            throw new IllegalArgumentException("\"error\": \"SlotDto should not be null\"");
        }

        List<Vehicle> allVehicles = getAllVehicles();

        Predicate<Vehicle> isSameSlotType = s -> s.getVehicleType().equals(vehicleDto.vehicleType());

        int takenSlots = (int) allVehicles.stream().filter(isSameSlotType.and(isTaken())).count();
        int slotsPerType = switch (vehicleDto.vehicleType()) {
            case CAR -> carSlots;
            case BUS -> busSlots;
            case OTHER -> 0;
        };

        int availableSlots = slotsPerType - takenSlots;

        if (availableSlots <= 0) {
            throw new NoAvailableSlotsException(String.format("%s parking slots exhausted! We are sorry.", vehicleDto.vehicleType()));
        }

        String regNumIncomingCar = vehicleDto.registrationNumber();

        if (regNumIncomingCar != null) {
            Optional<Vehicle> vehicleWithSameRegNum = allVehicles.stream()
                    .filter(s -> regNumIncomingCar.equals(s.getRegistrationNumber()))
                    .findFirst();

            if (vehicleWithSameRegNum.isPresent()) {
                Vehicle currVehicleInParkingSlot = vehicleWithSameRegNum.get();

                if (currVehicleInParkingSlot.getCheckIn() != null) {
                    throw new DuplicateRegistrationNumberException(String.format("Vehicle with registration number \"%s\" is already present in the parking lot", vehicleDto.registrationNumber()));
                } else {
                    Vehicle vehicleSlotIdToModify = vehicleRepo.getReferenceById(currVehicleInParkingSlot.getSlotId());
                    vehicleSlotIdToModify.resetParkedTime();
                    return slotDtoMapper.toTicket(vehicleSlotIdToModify);
                }
            }
        }
        // Label-1:
        Optional<Vehicle> freeSlotIdToUpdate = allVehicles.stream()
                .filter(isSameSlotType.and(isTaken().negate()))
                .findFirst();

        if (freeSlotIdToUpdate.isEmpty()) {
            // Leave only the following row after Label-1 for auto increment after every new car [not preferable]
            return slotDtoMapper.toTicket(vehicleRepo.save(slotDtoMapper.toVehicle(vehicleDto)));
        }

        Vehicle vehicleToSaveWithSpecifiedId = freeSlotIdToUpdate.get();

        VehicleDto vehicleToSaveDto = new VehicleDto(vehicleDto.vehicleType(), vehicleDto.registrationNumber());

        Vehicle vehicleToSave = Vehicle.of(vehicleToSaveDto);
        vehicleToSave.setSlotId(vehicleToSaveWithSpecifiedId.getSlotId());
        vehicleToSave.resetParkedTime();

        return slotDtoMapper.toTicket(vehicleRepo.save(vehicleToSave));
    }

    public List<TicketDto> checkInMultiply(VehicleDto... vehicleDtoS) {
        if (vehicleDtoS == null) {
            throw new IllegalArgumentException("\"error\": \"SlotsDTO should not be null\"");
        }

        List<TicketDto> ticketsToReturn = new ArrayList<>();

        for (VehicleDto v : vehicleDtoS) {
            if (v != null) {
                ticketsToReturn.add(checkInSingle(v));
            }
        }

        return ticketsToReturn;
    }

    public ReportDto checkOut(int slotId) {
        if (slotId <= 0) {
            throw new IllegalArgumentException("Parking slot id must be positive number");
        }

        Optional<Vehicle> vehicleLeaving = getCurrentParkingState().stream().filter(s -> s.getSlotId().equals(slotId)).findAny();

        if (vehicleLeaving.isEmpty()) {
            throw new NotAvailableVehicleInTheParkingException(String.format("There is no vehicle with ID = %d in parking lot.", slotId));
        }

        Vehicle vehicleBySlotId = vehicleRepo.getReferenceById(slotId);

        ReportDto reportDTO = ReportDto.of(
                vehicleBySlotId.getCheckIn(),
                LocalDateTime.now(),
                vehicleBySlotId.getRegistrationNumber(),
                vehicleBySlotId.getVehicleType()
        );

        vehicleBySlotId.nullParkedTime(); // set the parking slot free! (after we extracted it)

        return reportDtoMapper.toReportDTO(reportService.save(reportDtoMapper.toReport(reportDTO)));
    }

    private List<Vehicle> getAllVehicles() {
        return vehicleRepo.findAll().stream().toList();
    }

    private Predicate<Vehicle> isTaken() {
        return s -> s.getCheckIn() != null;
    }
}
