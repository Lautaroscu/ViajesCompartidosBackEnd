package com.viajes.viajesCompartidos.DTO;

import lombok.Getter;

@Getter
public class TripPassengerDTO {
    private int tripID;
    private int passengerID;

    public TripPassengerDTO(int tripID, int passengerID) {
        this.tripID = tripID;
        this.passengerID = passengerID;
    }
}
