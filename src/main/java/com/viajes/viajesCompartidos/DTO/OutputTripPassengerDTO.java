package com.viajes.viajesCompartidos.DTO;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.entities.Trip;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class OutputTripPassengerDTO implements Serializable {
    private User user;
    private int tripId;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    public OutputTripPassengerDTO(User user, Trip trip) {
        this.user = user;
        this.origin = trip.getOrigin();
        this.destination = trip.getDestination();
        this.tripId = trip.getTripId();
        this.departureTime = trip.getDate();
    }
}
