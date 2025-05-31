package com.viajes.viajesCompartidos.DTO.trip;

import lombok.Data;

import java.io.Serializable;
@Data
public class CompletedTripsQuantityDTO implements Serializable {
    private Long quantity;

    public CompletedTripsQuantityDTO(Long quantity) {
        this.quantity = quantity;
    }

}
