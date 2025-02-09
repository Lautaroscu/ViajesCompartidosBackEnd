package com.viajes.viajesCompartidos.exceptions;

public class TooManyAttemptsException extends RuntimeException {
    public TooManyAttemptsException(String mensaje) {
        super(mensaje);
    }
}
