package com.viajes.viajesCompartidos.exceptions.users;

public class InvalidUserEmailException extends RuntimeException{
    public InvalidUserEmailException(String message){
        super(message);
    }
}
