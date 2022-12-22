package com.pros.parkinglot.util;

import com.pros.parkinglot.model.slot.type.VehicleType;
import lombok.Getter;
import org.hibernate.cfg.NotYetImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Getter
public class PriceCalculator {
    private static final int MINUTES_PER_HOUR = 60;
    private static final int HOURS_PER_DAY = 24;

    public final double pricePerHourCar;
    public final double pricePerDayCar;

    public final double pricePerHourBus;
    public final double pricePerDayBus;

    @Autowired
    public PriceCalculator(
            @Qualifier("getPricePerHourCar") double pricePerHourCar,
            @Qualifier("getPricePerDayCar") double pricePerDayCar,
            @Qualifier("getPricePerHourBus") double pricePerHourBus,
            @Qualifier("getPricePerDayBus") double pricePerDayBus
    ) {
        this.pricePerHourCar = pricePerHourCar;
        this.pricePerDayCar = pricePerDayCar;
        this.pricePerHourBus = pricePerHourBus;
        this.pricePerDayBus = pricePerDayBus;
    }

    public BigDecimal calcPrice(long minutes, VehicleType vehicleType) {
        double pricePerHour = switch (vehicleType) {
            case CAR -> pricePerHourCar;
            case BUS -> pricePerHourBus;
            case OTHER -> throw new NotYetImplementedException("Not recognized vehicle type.");
        };

        double pricePerDay = switch (vehicleType) {
            case CAR -> pricePerDayCar;
            case BUS -> pricePerDayBus;
            case OTHER -> throw new NotYetImplementedException("Not recognized vehicle type.");
        };

        long totalHours = minutes / MINUTES_PER_HOUR;
        long numOfDays = totalHours / HOURS_PER_DAY;
        long hoursToPay = totalHours + ((minutes % MINUTES_PER_HOUR) > 0 ? 1 : 0);
        long hoursToPayAfterDaysExtracted = hoursToPay % HOURS_PER_DAY;

        BigDecimal totalPrice1 = new BigDecimal(numOfDays + 1).multiply(BigDecimal.valueOf(pricePerDay));
        BigDecimal totalPriceDaysTemp = BigDecimal.valueOf(numOfDays).multiply(BigDecimal.valueOf(pricePerDay));
        BigDecimal totalPriceHoursTemp = BigDecimal.valueOf(pricePerHour).multiply(BigDecimal.valueOf(hoursToPayAfterDaysExtracted));
        BigDecimal totalPrice2 = totalPriceDaysTemp.add(totalPriceHoursTemp);

        if (totalPrice2.equals(new BigDecimal("0.0"))) {
            totalPrice2 = BigDecimal.valueOf(pricePerHour);
        }

        return totalPrice1.compareTo(totalPrice2) < 0 ? totalPrice1 : totalPrice2;
    }
}
