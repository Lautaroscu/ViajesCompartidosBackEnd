package com.viajes.viajesCompartidos.DTO.trip;

import com.viajes.viajesCompartidos.DTO.chat.ChatDTO;
import com.viajes.viajesCompartidos.entities.Trip;
import com.viajes.viajesCompartidos.enums.TripStatus;
import lombok.Getter;
import com.viajes.viajesCompartidos.entities.User;

import java.io.Serializable;

@Getter
public class OutputTripDTO  extends InputTripDTO implements Serializable  {
    private int tripId;
    private int passengersCount;
    private ChatDTO chat;

    public OutputTripDTO()  {}
    public OutputTripDTO(Trip trip) {
       super(trip.getOrigin() , trip.getDestination() , trip.getDate() ,trip.getOwner().getUserId(),trip.getMaxPassengers() ,trip.getPrice() ,trip.getComment() , trip.getStatus() , trip.isPrivate());
       this.tripId = trip.getTripId();
       this.passengersCount = trip.getCountPassengers();

    }

    public void setChat(ChatDTO chat) {
        this.chat = chat;
    }
}
