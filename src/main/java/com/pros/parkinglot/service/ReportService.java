package com.pros.parkinglot.service;

import com.pros.parkinglot.dto.ReportDto;
import com.pros.parkinglot.mapper.ReportDtoMapper;
import com.pros.parkinglot.model.report.Report;
import com.pros.parkinglot.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    public List<ReportDto> getAllReportsInTimeRange(LocalDate in, LocalDate out) {
        Predicate<Report> inRangeLeft = r -> r.getCheckIn().isAfter(
                LocalDateTime.of(in.getYear(), in.getMonth(), in.getDayOfMonth(), 0, 0, 0)
        );

        Predicate<Report> inRangeRight = r -> r.getCheckOut().isBefore(
                LocalDateTime.of(out.getYear(), out.getMonth(), in.equals(out) ? out.getDayOfMonth() : out.getDayOfMonth() + 1, 0, 0, 0)
        );

        return reportRepo.findAll().stream().filter(inRangeLeft.and(inRangeRight)).map(mapper::toReportDTO).toList();
    }

    public List<ReportDto> getAllReportsWithRegistrationNumberWithStartingPrefix(List<String> regPrefixes) {
        if (regPrefixes == null || regPrefixes.isEmpty()) {
            return reportRepo.findAll().stream().map(mapper::toReportDTO).toList();
        }

        return reportRepo.findAll()
                .stream()
                .filter(r -> startsWithAnyOf(r.getRegistrationNumber(), regPrefixes))
                .map(mapper::toReportDTO)
                .toList();
    }

    public void writeAndClearReports(String fileName) {
        Path filePath = Path.of("src", "main", "resources", "logs", fileName);

        File targetFile = new File(filePath.getParent().toString());
        boolean createDirHierarchy = targetFile.mkdirs();

        try {
            boolean createFileLog = targetFile.createNewFile();
        } catch (IOException e) {
            throw new IllegalStateException(String.format("A problem occurred while creating file %s.", filePath.getFileName()));
        }

        try (var bw = Files.newBufferedWriter(filePath)) {
            List<Report> reports = reportRepo.findAll().stream().toList();

            bw.write(getHeader()+System.lineSeparator());

            for (Report r : reports) {
                bw.write(mapper.toCsvString(r));
            }

            bw.flush();
        } catch (IOException e) {
            throw new IllegalStateException(String.format("A problem occurred while writing to file %s.", filePath.getFileName()));
        }

        reportRepo.deleteAll();
    }

    private boolean startsWithAnyOf(String regNum, List<String> prefixes) {
        prefixes = prefixes.stream().filter(p -> p != null && !p.isEmpty() && !p.isBlank()).toList();

        if (regNum == null || regNum.isEmpty()) return false;

        for (String p : prefixes) {
            if (!regNum.startsWith(p)) {
                return false;
            }
        }

        return true;
    }

    private String getHeader() {
        return "Report Id,Car Type,Registration Number,Check In,Check Out,Price in EUR";
    }
}
