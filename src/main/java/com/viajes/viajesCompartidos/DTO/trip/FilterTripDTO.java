package com.viajes.viajesCompartidos.DTO.trip;

import com.viajes.viajesCompartidos.enums.TripType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class FilterTripDTO implements Serializable {
    private String origin , destination ;
    private Integer max_passengers , userId;
    private LocalDateTime startDate, endDate ;
    private Double maxPrice;
    private TripType tripType;
    private String strict;


}
