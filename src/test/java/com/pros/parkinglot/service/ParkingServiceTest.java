package com.pros.parkinglot.service;

import com.pros.parkinglot.configuration.DataPopulator;

import com.pros.parkinglot.dto.TicketDto;
import com.pros.parkinglot.dto.VehicleDto;
import com.pros.parkinglot.exception.DuplicateRegistrationNumberException;
import com.pros.parkinglot.exception.NoAvailableSlotsException;
import com.pros.parkinglot.configuration.mapper.ReportDtoMapper;
import com.pros.parkinglot.configuration.mapper.SlotDtoMapper;
import com.pros.parkinglot.model.slot.Bus;
import com.pros.parkinglot.model.slot.Car;
import com.pros.parkinglot.model.slot.Vehicle;
import com.pros.parkinglot.model.slot.type.VehicleType;
import com.pros.parkinglot.repository.ReportRepository;
import com.pros.parkinglot.repository.VehicleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:application.properties")
public class ParkingServiceTest {
    @Value("${parking.lot.car.slots}")
    private int carSlots;
    @Value("${parking.lot.bus.slots}")
    private int busSlots;
    @Value("${parking.lot.greeting.msg}")
    private String greetingMsg;

    @Mock
    private VehicleRepository vehicleRepo;

    @Mock
    private ReportRepository reportRepo;

    @Mock
    private ReportService reportService;

    @Mock
    private SlotDtoMapper slotDtoMapper;

    @Mock
    private ReportDtoMapper reportDtoMapper;

    private ParkingService parkingService;

    @InjectMocks
    private DataPopulator dataPopulator;

    @BeforeEach
    public void setUp() {
        parkingService = new ParkingService(
                vehicleRepo, reportService, slotDtoMapper, reportDtoMapper, carSlots, busSlots
        );
    }

    @Test
    public void testCheckInSingleWithNullDto() {
        assertThrows(IllegalArgumentException.class, () -> parkingService.checkInSingle(null),
                "Expected IllegalArgumentException to be thrown when trying to invoke checkInSingle with null VehicleDto");
    }

    @Test
    public void testCheckInSingleWithNoAvailableCarSlots() {
        when(vehicleRepo.findAll()).thenReturn(getParkingLotStateWithNoAvailableCarSlots());
        assertThrows(NoAvailableSlotsException.class, () -> parkingService.checkInSingle(new VehicleDto(VehicleType.CAR, "TEST")),
                "Expected NoAvailableSlotsException to be thrown when trying to invoke checkInSingle when parking has 50 occupied car slots");
    }

    @Test
    public void testCheckInSingleWithNoAvailableBusSlots() {
        when(vehicleRepo.findAll()).thenReturn(getParkingLotStateWithNoAvailableBusSlots());
        assertThrows(NoAvailableSlotsException.class, () -> parkingService.checkInSingle(new VehicleDto(VehicleType.BUS, "TEST")),
                "Expected NoAvailableSlotsException to be thrown when trying to invoke checkInSingle when parking has 50 occupied car slots");
    }

    @Test
    public void testCheckInSingleWithAlreadyExistingCarRegistrationNumber() {
        when(vehicleRepo.findAll()).thenReturn(List.of(dataPopulator.getVehicles()));
        assertThrows(DuplicateRegistrationNumberException.class, () -> parkingService.checkInSingle(new VehicleDto(VehicleType.CAR, "CA3434TA")),
                "Expected Car with registration number \"CA3434TA\" to be already present in the parking lot.");
    }

    @Test
    public void testCheckInSingleWithPlacingCarToSpecifiedSlotWithSameRegNum() {
        int slotId = 1;
        String regNum = "A1234BC";

        Vehicle vehicle = new Car(regNum);
        vehicle.setSlotId(1);
        vehicle.nullParkedTime();

        TicketDto ticketDto = new TicketDto(VehicleType.CAR, greetingMsg, regNum, slotId, LocalDateTime.now());

        when(vehicleRepo.findAll()).thenReturn(List.of(vehicle));
        when(vehicleRepo.getReferenceById(slotId)).thenReturn(vehicle);
        when(slotDtoMapper.toTicket(vehicle)).thenReturn(ticketDto);

        assertEquals(slotDtoMapper.toTicket(vehicle), parkingService.checkInSingle(new VehicleDto(VehicleType.CAR, regNum)),
                "Expected returned ticket to be for the specified car parked in a specified place.");
    }

    @Test
    public void testCheckInSingleWithRandomNewCar() {
        int slotId = 1;
        String regNum = "A1234BC";

        Vehicle vehicle = new Car(regNum);
        vehicle.setSlotId(1);
        vehicle.nullParkedTime();

        VehicleDto vehicleDto = new VehicleDto(VehicleType.CAR, regNum);
        TicketDto ticketDto = new TicketDto(VehicleType.CAR, greetingMsg, regNum, slotId, LocalDateTime.now());

        when(vehicleRepo.findAll()).thenReturn(List.of());
        when(slotDtoMapper.toVehicle(vehicleDto)).thenReturn(vehicle);
        when(vehicleRepo.save(vehicle)).thenReturn(vehicle);
        when(slotDtoMapper.toTicket(vehicle)).thenReturn(ticketDto);

        parkingService.checkInSingle(vehicleDto);

        assertEquals(slotDtoMapper.toTicket(vehicle), parkingService.checkInSingle(vehicleDto),
                "Expected returned ticket to be for the specified car parked in a random place.");
    }


    private List<Vehicle> getParkingLotStateWithNoAvailableCarSlots() {
        List<Vehicle> vehicles = new ArrayList<>(List.of(dataPopulator.getVehicles()));
        vehicles.addAll(getAdditionalCars());

        return vehicles;
    }

    private List<Vehicle> getParkingLotStateWithNoAvailableBusSlots() {
        List<Vehicle> vehicles = new ArrayList<>(List.of(dataPopulator.getVehicles()));
        vehicles.addAll(getAdditionalBus());

        return vehicles;
    }


    private List<Vehicle> getAdditionalCars() {
        return List.of(
                new Car(LocalDateTime.now().minusDays(1).minusHours(2), "CA2301CA"),
                new Car(LocalDateTime.now().minusDays(1).minusHours(3), "CA2302TA"),
                new Car(LocalDateTime.now().minusDays(2).minusHours(4), "PB2303BB"),
                new Car(LocalDateTime.now().minusDays(2).minusHours(1), "A2304P"),
                new Car(LocalDateTime.now().minusDays(3).minusHours(2), "A2305PB"),
                new Car(LocalDateTime.now().minusDays(5).minusHours(4), "B2306CC"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(10), "CA2307TC"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(15), "H82308C"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(20), "M2309HH"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(25), "BT2310EC"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(30), "E2311KH"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(35), "CB2312AA"),
                new Car(LocalDateTime.now().minusMinutes(10), "BT2313PO"),
                new Car(LocalDateTime.now().minusMinutes(12), "CB2314TK"),
                new Car(LocalDateTime.now().minusMinutes(15), "CB2315MK"),
                new Car(LocalDateTime.now().minusMinutes(20), "A2316CA"),
                new Car(LocalDateTime.now().minusMinutes(25), "PB2317CE"),
                new Car(LocalDateTime.now().minusMinutes(49), "CB2318EA"),
                new Car(LocalDateTime.now().minusDays(1).minusHours(2), "BP2319CA"),
                new Car(LocalDateTime.now().minusDays(1).minusHours(3), "CA2320TA"),
                new Car(LocalDateTime.now().minusDays(2).minusHours(4), "PB2321BB"),
                new Car(LocalDateTime.now().minusDays(2).minusHours(1), "A2322PP"),
                new Car(LocalDateTime.now().minusDays(3).minusHours(2), "A2323PB"),
                new Car(LocalDateTime.now().minusDays(5).minusHours(4), "B2324CC"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(10), "CA2325TC"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(15), "H2326CC"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(20), "M2327HH"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(25), "BT2328EC"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(30), "E2329KH"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(35), "CB2300AA"),
                new Car(LocalDateTime.now().minusMinutes(10), "BT2331PO"),
                new Car(LocalDateTime.now().minusMinutes(12), "CB2332TK")
        );
    }

    private List<Vehicle> getAdditionalBus() {
        return List.of(
                new Bus(LocalDateTime.now().minusDays(1).minusHours(2), "T0093AA")
        );
    }
}
