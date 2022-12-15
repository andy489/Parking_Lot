package com.pros.parkinglot.config;

import com.pros.parkinglot.exception.DuplicateRegistrationNumberException;
import com.pros.parkinglot.exception.NoAvailableSlotsException;
import com.pros.parkinglot.exception.NotAvailableVehicleInTheParkingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NoAvailableSlotsException.class})
    protected ResponseEntity<Object> handleConflictNoAvailableSlots(RuntimeException e, WebRequest request) {
        return handleExceptionInternal(e, String.format("{ \"error\": \"%s\" }", e.getMessage()),
                new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, request);
    }

    @ExceptionHandler(value = {DuplicateRegistrationNumberException.class})
    protected ResponseEntity<Object> handleConflictDuplicateRegistrationNumber(RuntimeException e, WebRequest request) {
        return handleExceptionInternal(e, String.format("{ \"error\": \"%s\" }", e.getMessage()),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {NotAvailableVehicleInTheParkingException.class})
    protected ResponseEntity<Object> handleConflictCheckOutNotExistingCarInTheParkingLot(RuntimeException e, WebRequest request) {
        return handleExceptionInternal(e, String.format("{ \"error\": \"%s\" }", e.getMessage()),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }


    @ExceptionHandler(value = {RuntimeException.class})
    protected ResponseEntity<Object> handleConflict(RuntimeException e, WebRequest request) {

        String bodyOfResponse = String.format("{ \"error\": \"%s\" }", e.getMessage());

        return handleExceptionInternal(e, bodyOfResponse,
                new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
