package com.pros.parkinglot.model.slot;

import com.pros.parkinglot.model.slot.type.SlotType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "slots")
public class Slot implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer slotId;

    @Column(name = "registration")
    private String registrationNumber;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private SlotType slotType;

    @Column(name= "in_time")
    private LocalDateTime parkedTime;

    public Slot() {
    }

    @Autowired
    public Slot(String registrationNumber, SlotType slotType) {
        this.registrationNumber = registrationNumber;
        this.slotType=slotType;
        this.parkedTime = LocalDateTime.now();
    }
}
