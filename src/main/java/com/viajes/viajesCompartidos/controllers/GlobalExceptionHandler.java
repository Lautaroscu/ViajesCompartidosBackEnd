package com.viajes.viajesCompartidos.controllers;

import com.viajes.viajesCompartidos.exceptions.InvalidCredentialsException;
import com.viajes.viajesCompartidos.exceptions.InvalidJoinRequestException;
import com.viajes.viajesCompartidos.exceptions.JoinRequestNotFoundException;
import com.viajes.viajesCompartidos.exceptions.TooManyAttemptsException;
import com.viajes.viajesCompartidos.exceptions.location.LocationNotFoundException;
import com.viajes.viajesCompartidos.exceptions.trips.MaxPassengersOnBoardException;
import com.viajes.viajesCompartidos.exceptions.trips.TripContainsPassangersException;
import com.viajes.viajesCompartidos.exceptions.trips.TripNotFoundException;
import com.viajes.viajesCompartidos.exceptions.users.NotEnoughBalanceException;
import com.viajes.viajesCompartidos.exceptions.users.UserAlreadyExistsException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(TripNotFoundException.class)
    public ResponseEntity<String> handleTripNotFound(TripNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<String> handleLocationNotFound(LocationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
    @ExceptionHandler(MaxPassengersOnBoardException.class)
    public ResponseEntity<String> handleMaxPassengersOnBoard(MaxPassengersOnBoardException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(TripContainsPassangersException.class)
    public ResponseEntity<String> handleTripContainsPassangers(TripContainsPassangersException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(NotEnoughBalanceException.class)
    public ResponseEntity<String> handleNotEnoughBalance(NotEnoughBalanceException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(InvalidJoinRequestException.class)
    public ResponseEntity<String> handleInvalidJoinRequest(InvalidJoinRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(JoinRequestNotFoundException.class)
    public ResponseEntity<String> handleJoinRequestNotFound(JoinRequestNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(TooManyAttemptsException.class)
    public ResponseEntity<String> handleTooManyAttempts(TooManyAttemptsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + ex.getMessage());
    }
}
