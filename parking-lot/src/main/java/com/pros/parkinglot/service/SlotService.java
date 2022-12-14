package com.pros.parkinglot.service;

import com.pros.parkinglot.dto.ReportDTO;
import com.pros.parkinglot.dto.VehicleDTO;
import com.pros.parkinglot.dto.Ticket;
import com.pros.parkinglot.exception.DuplicateRegistrationNumberException;
import com.pros.parkinglot.exception.NoAvailableSlotsException;
import com.pros.parkinglot.exception.NotAvailableVehicleInTheParkingException;
import com.pros.parkinglot.mapper.ReportDTOMapper;
import com.pros.parkinglot.mapper.SlotDtoMapper;
import com.pros.parkinglot.model.report.Report;
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
public class SlotService {
    @Value("${parking.lot.all.slots}")
    private int ALL_SLOTS;
    @Value("${parking.lot.car.slots}")
    private int CAR_SLOTS;
    @Value("${parking.lot.bus.slots}")
    private int BUS_SLOTS;

    private static Predicate<Vehicle> isTaken = s -> s.getParkedTime() != null;

    private VehicleRepository vehicleRepo;
    private ReportService reportService;
    private SlotDtoMapper slotDtoMapper;
    private ReportDTOMapper reportDTOMapper;

    @Autowired
    public SlotService(
            VehicleRepository vehicleRepo,
            ReportService reportService,
            SlotDtoMapper slotDtoMapper,
            ReportDTOMapper reportDTOMapper
    ) {
        this.vehicleRepo = vehicleRepo;
        this.reportService = reportService;
        this.slotDtoMapper = slotDtoMapper;
        this.reportDTOMapper = reportDTOMapper;
    }

    public List<Vehicle> getCurrentParkingState() {
        return getAllSlots().stream().filter(isTaken).collect(Collectors.toList());
    }

    public Ticket checkIn(VehicleDTO vehicleDTO) {
        if (vehicleDTO == null) {
            throw new IllegalArgumentException("\"error\": \"SlotDTO should not be null\"");
        }

        List<Vehicle> allSlots = getAllSlots();

        Predicate<Vehicle> isSameSlotType = s -> s.getVehicleType().equals(vehicleDTO.getVehicleType());

        int takenSlots = (int) allSlots.stream().filter(isSameSlotType.and(isTaken)).count();
        int availableSlots = CAR_SLOTS - takenSlots;

        if (availableSlots <= 0) {
            throw new NoAvailableSlotsException(String.format("{ \"error\": \"%s parking slots exhausted! We are sorry.\" }", vehicleDTO.getVehicleType()));
        }

        String regNumIncomingCar = vehicleDTO.getRegistrationNumber();
        Optional<String> regNum = allSlots.stream().map(Vehicle::getRegistrationNumber).filter(s -> s.equals(regNumIncomingCar)).findFirst();

        if (regNum.isPresent()) {
            throw new DuplicateRegistrationNumberException(String.format("{ \"error\": \"Vehicle with registration number %s is already present in parking lot\" }", vehicleDTO.getRegistrationNumber()));
        }

        return slotDtoMapper.toTicket(vehicleRepo.save(slotDtoMapper.toVehicle(vehicleDTO)));
    }

    public List<Ticket> checkIn(VehicleDTO... vehicleDTOs) {
        if (vehicleDTOs == null) {
            throw new IllegalArgumentException("\"error\": \"SlotsDTO should not be null\"");
        }

        List<Ticket> ticketsToReturn = new ArrayList<>();

        for (VehicleDTO v : vehicleDTOs) {
            if (v != null) {
                ticketsToReturn.add(checkIn(v));
            }
        }

        return ticketsToReturn;
    }

    public ReportDTO checkOut(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("Parking slot id must be positive number");
        }

        Optional<Vehicle> vehicleLeaving = getCurrentParkingState().stream().filter(s -> s.getSlotId().equals(id)).findAny();

        if (vehicleLeaving.isEmpty()) {
            throw new NotAvailableVehicleInTheParkingException(String.format("There is no vehicle parked in slot with Id=%d.", id));
        }

        Vehicle vehicleBySlotId = vehicleRepo.getReferenceById(id);

        ReportDTO reportDTO = new ReportDTO(
                vehicleBySlotId.getParkedTime(),
                LocalDateTime.now(),
                vehicleBySlotId.getRegistrationNumber(),
                vehicleBySlotId.getVehicleType()
        );

        vehicleBySlotId.resetParkedTime(); /** set the parking slot free! (after we extracted it)*/

        return reportDTOMapper.toReportDTO(reportService.save(reportDTOMapper.toReport(reportDTO)));
    }

    private List<Vehicle> getAllSlots() {
        return vehicleRepo.findAll().stream().toList();
    }
}
