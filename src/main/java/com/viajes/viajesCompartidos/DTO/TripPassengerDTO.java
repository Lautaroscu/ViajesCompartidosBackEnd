package com.viajes.viajesCompartidos.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class TripPassengerDTO implements Serializable {
    private int tripId;
    private int userId;

    public TripPassengerDTO(int tripId, int userId) {
        this.tripId = tripId;
        this.userId = userId;
    }
}
