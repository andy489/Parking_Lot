package com.pros.parkinglot.mapper;

import com.pros.parkinglot.dto.ReportDTO;
import com.pros.parkinglot.model.report.Report;
import com.pros.parkinglot.model.slot.type.VehicleType;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class ReportDTOMapperTest {

    ReportDTOMapper mapper = new ReportDTOMapper();

    @Test
    void testCalcPrice(){
        LocalDateTime in = LocalDateTime.now().minusSeconds(30);
        LocalDateTime out = LocalDateTime.now();

        long between = ChronoUnit.MINUTES.between(in, out);

        ReportDTO reportDTO = new ReportDTO(
                in,
                out,
                "СА1234СВ",
                VehicleType.CAR
        );


        Report report = mapper.toReport(reportDTO);
    }
}
