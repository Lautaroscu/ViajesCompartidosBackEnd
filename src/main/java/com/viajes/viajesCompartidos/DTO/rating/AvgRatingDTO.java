package com.viajes.viajesCompartidos.DTO.rating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AvgRatingDTO implements Serializable {
    private Double rating;

}
