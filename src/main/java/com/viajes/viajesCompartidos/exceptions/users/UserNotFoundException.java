package com.viajes.viajesCompartidos.exceptions.users;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){
        super(message);
    }
}
