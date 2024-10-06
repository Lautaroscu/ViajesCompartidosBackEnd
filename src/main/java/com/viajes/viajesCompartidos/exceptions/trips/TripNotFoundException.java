package com.viajes.viajesCompartidos.exceptions.trips;

public class TripNotFoundException extends RuntimeException {
    public TripNotFoundException(String message) {
        super(message);
    }
}
