package com.pros.parkinglot.repository;

import com.pros.parkinglot.model.slot.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Integer> {
}
