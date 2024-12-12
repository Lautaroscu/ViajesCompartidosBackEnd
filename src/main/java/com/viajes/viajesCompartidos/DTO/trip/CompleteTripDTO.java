package com.viajes.viajesCompartidos.DTO.trip;

import lombok.Data;

import java.io.Serializable;
@Data
public class CompleteTripDTO implements Serializable {
    private double userLatitude;
    private double userLongitude;
    private long userId;
    private double cityLatitude;
    private double cityLongitude;
}
