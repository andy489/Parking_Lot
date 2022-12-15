package com.pros.parkinglot.model.report;

import com.pros.parkinglot.model.slot.type.VehicleType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;


import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@ToString
@Table(name = "reports")
public class Report implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long reportId;

    @Column(name = "check_in")
    @NotNull
    private LocalDateTime checkIn;

    @Column(name = "check_out")
    @NotNull
    private LocalDateTime checkOut;

    @Column
    private String registrationNumber;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private VehicleType vehicleType;

    @Column
    private BigDecimal price;

    public Report() {
    }

    public Report(
            LocalDateTime checkIn,
            LocalDateTime checkOut,
            String registrationNumber,
            VehicleType vehicleType,
            BigDecimal price
    ) {
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
        this.price = price;
    }

    public Report(
            Long reportId,
            LocalDateTime checkIn,
            LocalDateTime checkOut,
            String registrationNumber,
            VehicleType vehicleType,
            BigDecimal price
    ) {
        this.reportId = reportId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
        this.price = price;
    }
}
