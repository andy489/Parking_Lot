package com.pros.parkinglot.service;

import com.pros.parkinglot.dto.ReportDto;
import com.pros.parkinglot.mapper.ReportDtoMapper;
import com.pros.parkinglot.model.report.Report;
import com.pros.parkinglot.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Predicate;

@Service
public class ReportService {

    private final ReportRepository reportRepo;
    private final ReportDtoMapper mapper;

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

    public List<ReportDto> getAllReportsInTimeRange(LocalDate in, LocalDate out) {
        Predicate<Report> inRangeLeft = r -> r.getCheckIn().isAfter(
                LocalDateTime.of(in.getYear(), in.getMonth(), in.getDayOfMonth(), 0, 0, 0)
        );

        Predicate<Report> inRangeRight = r -> r.getCheckOut().isBefore(
                LocalDateTime.of(out.getYear(), out.getMonth(), in.equals(out) ? out.getDayOfMonth() : out.getDayOfMonth() + 1, 0, 0, 0)
        );

        return reportRepo.findAll().stream().filter(inRangeLeft.and(inRangeRight)).map(mapper::toReportDTO).toList();
    }
}
