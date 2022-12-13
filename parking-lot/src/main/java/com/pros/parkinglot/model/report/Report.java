package com.pros.parkinglot.model.report;

import com.pros.parkinglot.model.vehicle.Vehicle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.NonNull;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long reportId;

    @Column
    @NotNull
    private LocalDateTime leaveTime;

    @Column
    private String registrationNumber;

    @Column
    @NotNull
    private BigDecimal price;

    public Report() {
    }

    public Report(LocalDateTime leaveTime, String registrationNumber, BigDecimal price) {
        this.leaveTime = leaveTime;
        this.registrationNumber = registrationNumber;
        this.price = price;
    }
}