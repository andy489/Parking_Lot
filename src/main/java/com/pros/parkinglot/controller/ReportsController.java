package com.pros.parkinglot.controller;

import com.pros.parkinglot.model.report.Report;
import com.pros.parkinglot.model.slot.type.VehicleType;
import com.pros.parkinglot.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = {"/reports"})
public class ReportsController {

    private final ReportService reportService;

    @Autowired
    public ReportsController(ReportService reportService){
        this.reportService=reportService;
    }

    @GetMapping(value = {"/template"})
    public Report templateCheat() {
        return new Report(
                17L,
                LocalDateTime.now().minusDays(2).minusHours(3),
                LocalDateTime.now(),
                "CB9989PT",
                VehicleType.BUS,
                new BigDecimal(95)
        );
    }

    @GetMapping
    public ResponseEntity<List<Report>> getAllReports() {
        return new ResponseEntity<>(reportService.getAllReports(), HttpStatus.OK);
    }

}
