package com.viajes.viajesCompartidos.DTO.geonames;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeoNamesResponse implements Serializable {
    // Getters y Setters
    private int totalResultsCount;
    private List<GeoCity> geonames;


}