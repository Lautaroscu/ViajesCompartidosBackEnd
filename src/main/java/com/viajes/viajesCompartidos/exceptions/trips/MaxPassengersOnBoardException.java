package com.viajes.viajesCompartidos.exceptions.trips;

public class MaxPassengersOnBoardException extends RuntimeException{
    public MaxPassengersOnBoardException(String message){
        super(message);
    }
}
