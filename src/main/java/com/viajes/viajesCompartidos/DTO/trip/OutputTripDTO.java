package com.viajes.viajesCompartidos.DTO.trip;

import com.viajes.viajesCompartidos.DTO.chat.ChatDTO;
import com.viajes.viajesCompartidos.DTO.location.OutputLocationDTO;
import com.viajes.viajesCompartidos.entities.Trip;
import com.viajes.viajesCompartidos.enums.TripStatus;
import lombok.Getter;
import com.viajes.viajesCompartidos.entities.User;

import java.io.Serializable;

@Getter
public class OutputTripDTO  implements Serializable  {
    private int tripId;
    private OutputLocationDTO origin_location;
    private OutputLocationDTO destination_location;
    private int passengersCount;
    private ChatDTO chat;

    public OutputTripDTO()  {}
    public OutputTripDTO(Trip trip) {
       this.tripId = trip.getTripId();
       this.passengersCount = trip.getCountPassengers();
       this.origin_location = new OutputLocationDTO(trip.getOrigin());
       this.destination_location = new OutputLocationDTO(trip.getDestination());

    }

    public void setChat(ChatDTO chat) {
        this.chat = chat;
    }
}
