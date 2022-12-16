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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ParkingService {
    @Value("${parking.lot.car.slots}")
    private int CAR_SLOTS;
    @Value("${parking.lot.bus.slots}")
    private int BUS_SLOTS;

    private final VehicleRepository vehicleRepo;
    private final ReportService reportService;
    private final SlotDtoMapper slotDtoMapper;
    private final ReportDtoMapper reportDtoMapper;

    @Autowired
    public ParkingService(
            VehicleRepository vehicleRepo,
            ReportService reportService,
            SlotDtoMapper slotDtoMapper,
            ReportDtoMapper reportDtoMapper
    ) {
        this.vehicleRepo = vehicleRepo;
        this.reportService = reportService;
        this.slotDtoMapper = slotDtoMapper;
        this.reportDtoMapper = reportDtoMapper;
    }

    public List<Vehicle> getCurrentParkingState() {
        return getAllSlots().stream().filter(isTaken()).collect(Collectors.toList());
    }

    public TicketDto checkIn(VehicleDto vehicleDto) {
        if (vehicleDto == null) {
            throw new IllegalArgumentException("\"error\": \"SlotDto should not be null\"");
        }

        List<Vehicle> allVehicles = getAllSlots();

        Predicate<Vehicle> isSameSlotType = s -> s.getVehicleType().equals(vehicleDto.getVehicleType());

        int takenSlots = (int) allVehicles.stream().filter(isSameSlotType.and(isTaken())).count();
        int slotsPerType = switch (vehicleDto.getVehicleType()) {
            case CAR -> CAR_SLOTS;
            case BUS -> BUS_SLOTS;
            case OTHER -> 0;
        };

        int availableSlots = slotsPerType - takenSlots;

        if (availableSlots <= 0) {
            throw new NoAvailableSlotsException(String.format("%s parking slots exhausted! We are sorry.", vehicleDto.getVehicleType()));
        }

        String regNumIncomingCar = vehicleDto.getRegistrationNumber();

        if (regNumIncomingCar!= null) {
            Optional<Vehicle> vehicleWithSameRegNum = allVehicles.stream()
                    .filter(s -> regNumIncomingCar.equals(s.getRegistrationNumber()))
                    .findFirst();

            if (vehicleWithSameRegNum.isPresent()) {
                Vehicle currVehicleInParkingSlot = vehicleWithSameRegNum.get();

                if (currVehicleInParkingSlot.getCheckIn() != null) {
                    throw new DuplicateRegistrationNumberException(String.format("Vehicle with registration number \"%s\" is already present in the parking lot", vehicleDto.getRegistrationNumber()));
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

        VehicleDto vehicleToSaveDto = new VehicleDto(vehicleDto.getVehicleType(), vehicleDto.getRegistrationNumber());

        Vehicle vehicleToSave = Vehicle.of(vehicleToSaveDto);
        vehicleToSave.setSlotId(vehicleToSaveWithSpecifiedId.getSlotId());
        vehicleToSave.resetParkedTime();

        return slotDtoMapper.toTicket(vehicleRepo.save(vehicleToSave));
    }

    public List<TicketDto> checkIn(VehicleDto... vehicleDtoS) {
        if (vehicleDtoS == null) {
            throw new IllegalArgumentException("\"error\": \"SlotsDTO should not be null\"");
        }

        List<TicketDto> ticketsToReturn = new ArrayList<>();

        for (VehicleDto v : vehicleDtoS) {
            if (v != null) {
                ticketsToReturn.add(checkIn(v));
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

        ReportDto reportDTO = new ReportDto(
                vehicleBySlotId.getCheckIn(),
                LocalDateTime.now(),
                vehicleBySlotId.getRegistrationNumber(),
                vehicleBySlotId.getVehicleType()
        );

        vehicleBySlotId.nullParkedTime(); // set the parking slot free! (after we extracted it)

        return reportDtoMapper.toReportDTO(reportService.save(reportDtoMapper.toReport(reportDTO)));
    }

    private List<Vehicle> getAllSlots() {
        return vehicleRepo.findAll().stream().toList();
    }

    private Predicate<Vehicle> isTaken() {
        return s -> s.getCheckIn() != null;
    }
}