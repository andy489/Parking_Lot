package com.pros.parkinglot.exception;

public class NoAvailableSlotsException extends RuntimeException {
    public NoAvailableSlotsException(String msg) {
        super(msg);
    }

    public NoAvailableSlotsException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
