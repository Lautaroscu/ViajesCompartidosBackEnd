package com.viajes.viajesCompartidos.controllers;

import com.viajes.viajesCompartidos.DTO.ErrorResponse;
import com.viajes.viajesCompartidos.exceptions.*;
import com.viajes.viajesCompartidos.exceptions.location.InvalidLocationException;
import com.viajes.viajesCompartidos.exceptions.location.LocationNotFoundException;
import com.viajes.viajesCompartidos.exceptions.trips.MaxPassengersOnBoardException;
import com.viajes.viajesCompartidos.exceptions.trips.TripContainsPassangersException;
import com.viajes.viajesCompartidos.exceptions.trips.TripNotFoundException;
import com.viajes.viajesCompartidos.exceptions.users.NotEnoughBalanceException;
import com.viajes.viajesCompartidos.exceptions.users.UserAlreadyExistsException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({
            UserAlreadyExistsException.class,
            MaxPassengersOnBoardException.class,
            NotEnoughBalanceException.class ,
            TripContainsPassangersException.class ,
            InvalidLocationException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler({
            TripNotFoundException.class ,
            UserNotFoundException.class ,
            EntityNotFoundException.class ,
            JoinRequestNotFoundException.class,
            LocationNotFoundException.class

    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }





    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return buildResponse("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> buildResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(new ErrorResponse(message, status.value()));
    }
}
