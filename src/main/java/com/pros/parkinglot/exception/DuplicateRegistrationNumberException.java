package com.pros.parkinglot.exception;

public class DuplicateRegistrationNumberException extends RuntimeException {
    public DuplicateRegistrationNumberException(String msg) {
        super(msg);
    }

    public DuplicateRegistrationNumberException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
