package com.viajes.viajesCompartidos.exceptions.trips;

public class InvalidLocationException extends RuntimeException {
    public InvalidLocationException(String message) {
        super(message);
    }
}
