package com.pros.parkinglot.controller;

import com.pros.parkinglot.dto.ReportDto;
import com.pros.parkinglot.dto.TimeRange;
import com.pros.parkinglot.model.report.Report;
import com.pros.parkinglot.model.slot.type.VehicleType;
import com.pros.parkinglot.service.ReportService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = {"/reports"})
public class ReportsController {

    private final ReportService reportService;

    @Autowired
    public ReportsController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping(value = {"/template"})
    public ResponseEntity<Report> templateReport() {
        return new ResponseEntity<>(new Report(
                17L,
                LocalDateTime.now().minusDays(2).minusHours(3),
                LocalDateTime.now(),
                "CB9989PT",
                VehicleType.BUS,
                new BigDecimal(95)
        ), HttpStatus.OK);
    }

    @GetMapping(value = {"/template-range"})
    public ResponseEntity<TimeRange> templateRange() {
        return new ResponseEntity<>(new TimeRange(
                LocalDate.now().minusDays(1),
                LocalDate.now()
        ), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<List<ReportDto>> getAllReportsInTimeRange(@RequestBody @Valid TimeRange range) {
        return new ResponseEntity<>(reportService.getAllReportsInTimeRange(range.getCheckIn(), range.getCheckOut()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReportDto>> getAllReportsWithRegistrationNumberWithStartingPrefix(
            @RequestParam(required = false) List<String> from
    ) {
        return new ResponseEntity<>(
                reportService.getAllReportsWithRegistrationNumberWithStartingPrefix(from),
                HttpStatus.OK
        );
    }

    @Transactional
    @DeleteMapping
    public ResponseEntity<String> writeAndClearReports() {
        Date date = new Date();
        String fileName = "log-" + date.getTime() + ".csv";

        reportService.writeAndClearReports(fileName);

        return new ResponseEntity<>(String.format("{ \"responseMsg\": \"Reports saved to file %s and cleared from DB.\"", fileName), HttpStatus.OK);
    }

}
