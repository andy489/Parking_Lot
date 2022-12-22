package com.pros.parkinglot.util;

import com.pros.parkinglot.model.report.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class CSVFormatter {
    private final String delimiter;
    private final DateTimeFormatter customFormat;

    @Autowired
    public CSVFormatter(
            @Qualifier("basicDateFormatter") DateTimeFormatter customFormat,
            @Qualifier("commaDelimiter") String delimiter
    ) {
        this.delimiter = delimiter;
        this.customFormat = customFormat;
    }

    public String toCsvString(Report report) {
        String in = report.getCheckIn().format(customFormat);
        String out = report.getCheckOut().format(customFormat);

        return String.format(
                "%d%s%s%s%s%s%s%s%s%s%s%n",
                report.getReportId(), delimiter,
                report.getVehicleType(), delimiter,
                report.getRegistrationNumber(), delimiter,
                in, delimiter,
                out, delimiter,
                report.getPrice().toString()
        );
    }
}
