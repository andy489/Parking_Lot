package com.pros.parkinglot.service;

import com.pros.parkinglot.mapper.ReportDtoMapper;
import com.pros.parkinglot.model.report.Report;
import com.pros.parkinglot.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private ReportRepository reportRepo;
    private ReportDtoMapper mapper;

    @Autowired
    public ReportService(ReportRepository reportRepo, ReportDtoMapper mapper) {
        this.reportRepo = reportRepo;
        this.mapper = mapper;
    }

    public Report save(Report report) {
        return reportRepo.save(report);
    }

    public List<Report> getAllReports() {
        return reportRepo.findAll();
    }
}
