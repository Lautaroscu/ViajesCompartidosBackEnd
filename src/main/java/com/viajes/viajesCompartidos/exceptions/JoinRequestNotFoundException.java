package com.viajes.viajesCompartidos.exceptions;

public class JoinRequestNotFoundException extends RuntimeException{
    public JoinRequestNotFoundException(
            String message
    ){
        super(message);
    }
}
