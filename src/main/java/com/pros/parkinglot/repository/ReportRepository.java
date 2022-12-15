package com.pros.parkinglot.repository;

import com.pros.parkinglot.model.report.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
