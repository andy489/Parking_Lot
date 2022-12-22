package com.pros.parkinglot.configuration;

import com.pros.parkinglot.dto.ReportDto;
import com.pros.parkinglot.mapper.ReportDtoMapper;
import com.pros.parkinglot.model.report.Report;
import com.pros.parkinglot.model.slot.Bus;
import com.pros.parkinglot.model.slot.Car;
import com.pros.parkinglot.model.slot.Vehicle;
import com.pros.parkinglot.model.slot.type.VehicleType;
import com.pros.parkinglot.repository.ReportRepository;
import com.pros.parkinglot.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataPopulator {

    private final VehicleRepository vehicleRepo;
    private final ReportRepository reportRepo;
    private final ReportDtoMapper mapper;

    @Autowired
    public DataPopulator(VehicleRepository vehicleRepo, ReportRepository reportRepo, ReportDtoMapper mapper) {
        this.vehicleRepo = vehicleRepo;
        this.reportRepo = reportRepo;
        this.mapper = mapper;
    }

    // Uncomment to populate DB

//    @PostConstruct
//    public void populateData() {
//        vehicleRepo.saveAll(Arrays.asList(getVehicles()));
//        reportRepo.saveAll(Arrays.asList(getReports()));
//    }

    public Vehicle[] getVehicles() {
        return new Vehicle[]{
                new Car(LocalDateTime.now().minusDays(1).minusHours(2), "BP4494CA"),
                new Car(LocalDateTime.now().minusDays(1).minusHours(3), "CA3434TA"),
                new Car(LocalDateTime.now().minusDays(2).minusHours(4), "PB1101BB"),
                new Car(LocalDateTime.now().minusDays(2).minusHours(1), "A4114PP"),
                new Car(LocalDateTime.now().minusDays(3).minusHours(2), "A1789PB"),
                new Car(LocalDateTime.now().minusDays(5).minusHours(4), "B1187CC"),

                new Car(LocalDateTime.now().minusHours(1).minusMinutes(10), "CA9078TC"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(15), "H8711CC"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(20), "M4590HH"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(25), "BT7614EC"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(30), "E1314KH"),
                new Car(LocalDateTime.now().minusHours(1).minusMinutes(35), "CB7113AA"),

                new Car(LocalDateTime.now().minusMinutes(10), "BT6676PO"),
                new Car(LocalDateTime.now().minusMinutes(12), "CB0978TK"),
                new Car(LocalDateTime.now().minusMinutes(15), "CB1111MK"),
                new Car(LocalDateTime.now().minusMinutes(20), "A6556CA"),
                new Car(LocalDateTime.now().minusMinutes(25), "PB4443CE"),
                new Car(LocalDateTime.now().minusMinutes(49), "CB0007EA"),

                new Bus(LocalDateTime.now().minusDays(1).minusHours(3), "K6971TT"),
                new Bus(LocalDateTime.now().minusDays(2).minusHours(2), "A8911PT"),
                new Bus(LocalDateTime.now().minusDays(3).minusHours(1), "B0032TAE"),

                new Bus(LocalDateTime.now().minusHours(2).minusMinutes(17), "CB1617AT"),
                new Bus(LocalDateTime.now().minusHours(3).minusMinutes(15), "PB931KO"),
                new Bus(LocalDateTime.now().minusHours(4).minusMinutes(13), "CB1173PC"),

                new Bus(LocalDateTime.now().minusMinutes(45), "CA1723PB"),
                new Bus(LocalDateTime.now().minusMinutes(35), "BT9993EE"),
                new Bus(LocalDateTime.now().minusMinutes(52), "CA9888TA")
        };
    }

    public Report[] getReports() {
        return new Report[]{
                mapper.toReport(ReportDto.of(
                        LocalDateTime.now().minusHours(15),
                        LocalDateTime.now().minusHours(12).minusMinutes(20), "A8809AB", VehicleType.CAR)),
                mapper.toReport(ReportDto.of(
                        LocalDateTime.now().minusHours(12),
                        LocalDateTime.now().minusHours(10), "CA1949BH", VehicleType.CAR)),
                mapper.toReport(ReportDto.of(
                        LocalDateTime.now().minusDays(1).minusHours(3),
                        LocalDateTime.now().minusDays(1).minusHours(1), "CB1120AB", VehicleType.BUS)),
                mapper.toReport(ReportDto.of(
                        LocalDateTime.now().minusDays(1).minusHours(4),
                        LocalDateTime.now().minusDays(1).minusHours(2), "B1319BB", VehicleType.CAR)),
                mapper.toReport(ReportDto.of(
                        LocalDateTime.now().minusDays(3).minusHours(3),
                        LocalDateTime.now().minusDays(2).minusHours(1), "CB8701AB", VehicleType.CAR)),
                mapper.toReport(ReportDto.of(
                        LocalDateTime.now().minusDays(2).minusHours(2),
                        LocalDateTime.now().minusDays(1).minusHours(4), "CA1345BH", VehicleType.CAR)),
                mapper.toReport(ReportDto.of(
                        LocalDateTime.now().minusHours(8).minusMinutes(18),
                        LocalDateTime.now().minusHours(2), "PB3888AB", VehicleType.BUS)),
                mapper.toReport(ReportDto.of(
                        LocalDateTime.now().minusDays(1).minusHours(1),
                        LocalDateTime.now().minusHours(6), "CA3310BB", VehicleType.CAR)),
                mapper.toReport(ReportDto.of(
                        LocalDateTime.now().minusDays(3).minusHours(1),
                        LocalDateTime.now().minusHours(5), "BT1101BP", VehicleType.CAR)),
                mapper.toReport(ReportDto.of(
                        LocalDateTime.now().minusDays(1).minusHours(1),
                        LocalDateTime.now().minusHours(2), "E1313BC", VehicleType.BUS))
        };
    }
}
