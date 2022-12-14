package com.pros.parkinglot.service;

import com.pros.parkinglot.mapper.ReportDTOMapper;
import com.pros.parkinglot.mapper.SlotDtoMapper;
import com.pros.parkinglot.model.report.Report;
import com.pros.parkinglot.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ReportService {

    private ReportRepository reportRepo;
    private ReportDTOMapper mapper;

    public ReportService(ReportRepository reportRepo, ReportDTOMapper mapper) {
        this.reportRepo = reportRepo;
        this.mapper = mapper;
    }

    public Report save(Report report) {
        return reportRepo.save(report);
    }
}
