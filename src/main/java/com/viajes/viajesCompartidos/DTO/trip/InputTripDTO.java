package com.viajes.viajesCompartidos.DTO.trip;

import com.viajes.viajesCompartidos.DTO.location.InputLocationDTO;
import com.viajes.viajesCompartidos.entities.Location;
import com.viajes.viajesCompartidos.enums.TripStatus;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class InputTripDTO implements Serializable {
    private InputLocationDTO origin;
    private InputLocationDTO destination;
    private LocalDateTime date;
    private int ownerId;
    private int maxPassengers;
    private double price;
    private String comment;
    private TripStatus status;
    private boolean isPrivate;

    public InputTripDTO() {}
    public InputTripDTO(InputLocationDTO origin, InputLocationDTO destination, LocalDateTime date, int ownerId,int maxPassengers , double price, String comment , TripStatus status , boolean isPrivate) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.ownerId = ownerId;
        this.maxPassengers = maxPassengers;
        this.price = price;
        this.comment = comment;
        this.status = status;
        this.isPrivate = isPrivate;

    }
}
