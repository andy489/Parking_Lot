package com.pros.parkinglot.repository;

import com.pros.parkinglot.model.slot.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SlotRepository extends JpaRepository<Slot, Integer> {
    List<Slot> findAll();
}
