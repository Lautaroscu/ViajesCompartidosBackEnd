package com.viajes.viajesCompartidos.DTO.trip;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class InputTripDTO implements Serializable {
    private String origin;
    private String destination;
    private LocalDateTime date;
    private int ownerId;
    private int maxPassengers;

    public InputTripDTO() {}
    public InputTripDTO(String origin, String destination, LocalDateTime date, int ownerId,int maxPassengers) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.ownerId = ownerId;
        this.maxPassengers = maxPassengers;

    }
}
