package com.pros.parkinglot.exception;

public class NotAvailableVehicleInTheParkingException extends RuntimeException {
    public NotAvailableVehicleInTheParkingException(String msg) {
        super(msg);
    }

    public NotAvailableVehicleInTheParkingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
