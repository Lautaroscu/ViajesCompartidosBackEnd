package com.viajes.viajesCompartidos.DTO.trip;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class FilterTripDTO {
    private String origin , destination ;
    private Integer max_passengers , userId;
    private LocalDateTime startDate, endDate ;


}
