package com.viajes.viajesCompartidos.exceptions;

public class InvalidJoinRequestException extends RuntimeException{
    public InvalidJoinRequestException(String msg){
        super(msg);
    }
}
