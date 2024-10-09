package com.viajes.viajesCompartidos.DTO.trip;

import com.viajes.viajesCompartidos.entities.Trip;
import lombok.Getter;
import com.viajes.viajesCompartidos.entities.User;

import java.io.Serializable;

@Getter
public class OutputTripDTO  extends InputTripDTO implements Serializable  {
    private int tripId;
    private int passengersCount;

    public OutputTripDTO()  {}
    public OutputTripDTO(Trip trip) {
       super(trip.getOrigin() , trip.getDestination() , trip.getDate() ,trip.getOwner().getUserId(),trip.getMaxPassengers());
       this.tripId = trip.getTripId();
       this.passengersCount = trip.getCountPassengers();
    }


}
