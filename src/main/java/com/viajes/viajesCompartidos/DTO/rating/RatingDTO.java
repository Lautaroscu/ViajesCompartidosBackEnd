package com.viajes.viajesCompartidos.DTO.rating;

import com.viajes.viajesCompartidos.entities.Rating;
import com.viajes.viajesCompartidos.repositories.RatingRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO implements Serializable {
    private Integer tripId;
    private Integer ratedUserId;
    private Integer ratingUserId;
    private Integer score; //1 - 5

    public RatingDTO(Rating r ) {
        this.tripId = r.getTrip().getTripId();
        this.ratedUserId = r.getUserRated().getUserId();
        this.ratingUserId = r.getUserRater().getUserId();
        this.score = r.getRating();
    }

}
