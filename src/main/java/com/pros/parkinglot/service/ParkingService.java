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
    @Value("${parking.lot.all.slots}")
    private int ALL_SLOTS;
    @Value("${parking.lot.car.slots}")
    private int CAR_SLOTS;
    @Value("${parking.lot.bus.slots}")
    private int BUS_SLOTS;

    private VehicleRepository vehicleRepo;
    private ReportService reportService;
    private SlotDtoMapper slotDtoMapper;
    private ReportDtoMapper reportDtoMapper;

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

    public TicketDto checkIn(VehicleDto vehicleDTO) {
        if (vehicleDTO == null) {
            throw new IllegalArgumentException("\"error\": \"SlotDTO should not be null\"");
        }

        List<Vehicle> allVehicles = getAllSlots();

        Predicate<Vehicle> isSameSlotType = s -> s.getVehicleType().equals(vehicleDTO.getVehicleType());

        int takenSlots = (int) allVehicles.stream().filter(isSameSlotType.and(isTaken())).count();
        int slotsPerType = switch (vehicleDTO.getVehicleType()) {
            case CAR -> CAR_SLOTS;
            case BUS -> BUS_SLOTS;
            case OTHER -> 0;
        };
        int availableSlots = slotsPerType - takenSlots;

        if (availableSlots <= 0) {
            throw new NoAvailableSlotsException(String.format("%s parking slots exhausted! We are sorry.", vehicleDTO.getVehicleType()));
        }

        String regNumIncomingCar = vehicleDTO.getRegistrationNumber();
        Optional<Vehicle> vehicleWithSameRegNum = allVehicles.stream()
                .filter(s -> s.getRegistrationNumber().equals(regNumIncomingCar))
                .findFirst();

        if (vehicleWithSameRegNum.isPresent()) {
            Vehicle currVehicleInParkingSlot = vehicleWithSameRegNum.get();

            if (currVehicleInParkingSlot.getParkedTime() != null) {
                throw new DuplicateRegistrationNumberException(String.format("Vehicle with registration number \"%s\" is already present in the parking lot", vehicleDTO.getRegistrationNumber()));
            } else {
                Vehicle vehicleSlotIdToModify = vehicleRepo.getReferenceById(currVehicleInParkingSlot.getSlotId());
                vehicleSlotIdToModify.resetParkedTime();
                return slotDtoMapper.toTicket(vehicleSlotIdToModify);
            }
        }

        return slotDtoMapper.toTicket(vehicleRepo.save(slotDtoMapper.toVehicle(vehicleDTO)));
    }

    public List<TicketDto> checkIn(VehicleDto... vehicleDtos) {
        if (vehicleDtos == null) {
            throw new IllegalArgumentException("\"error\": \"SlotsDTO should not be null\"");
        }

        List<TicketDto> ticketsToReturn = new ArrayList<>();

        for (VehicleDto v : vehicleDtos) {
            if (v != null) {
                ticketsToReturn.add(checkIn(v));
            }
        }

        return ticketsToReturn;
    }

    public ReportDto checkOut(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Parking slot id must be positive number");
        }

        Optional<Vehicle> vehicleLeaving = getCurrentParkingState().stream().filter(s -> s.getSlotId().equals(id)).findAny();

        if (vehicleLeaving.isEmpty()) {
            throw new NotAvailableVehicleInTheParkingException(String.format("There is no vehicle with ID = %d in parking lot.", id));
        }

        Vehicle vehicleBySlotId = vehicleRepo.getReferenceById(id);

        ReportDto reportDTO = new ReportDto(
                vehicleBySlotId.getParkedTime(),
                LocalDateTime.now(),
                vehicleBySlotId.getRegistrationNumber(),
                vehicleBySlotId.getVehicleType()
        );

        vehicleBySlotId.nullParkedTime(); /** set the parking slot free! (after we extracted it)*/

        return reportDtoMapper.toReportDTO(reportService.save(reportDtoMapper.toReport(reportDTO)));
    }

    private List<Vehicle> getAllSlots() {
        return vehicleRepo.findAll().stream().toList();
    }

    private Predicate<Vehicle> isTaken() {
        return s -> s.getParkedTime() != null;
    }
}
