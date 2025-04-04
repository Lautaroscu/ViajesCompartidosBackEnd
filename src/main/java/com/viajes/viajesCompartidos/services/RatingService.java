package com.viajes.viajesCompartidos.services;

import com.viajes.viajesCompartidos.DTO.rating.AvgRatingDTO;
import com.viajes.viajesCompartidos.DTO.rating.RatingDTO;
import com.viajes.viajesCompartidos.entities.Rating;
import com.viajes.viajesCompartidos.entities.Trip;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.repositories.RatingRepository;
import com.viajes.viajesCompartidos.repositories.TripRepository;
import com.viajes.viajesCompartidos.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    @Autowired
    public RatingService(RatingRepository ratingRepository, UserRepository userRepository, TripRepository tripRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
    }
    public RatingDTO save(RatingDTO inputRatingDTO) {
        User rated = userRepository.findById(inputRatingDTO.getRatedUserId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        User rater = userRepository.findById(inputRatingDTO.getRatingUserId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        Trip trip = tripRepository.findById(inputRatingDTO.getTripId()).orElseThrow(() -> new RuntimeException("Trip no encontrado"));
        Rating rating = new Rating();

            rating.setUserRated(rated);
            rating.setUserRater(rater);
            rating.setRating(inputRatingDTO.getScore());
            rating.setTrip(trip);
           rating = ratingRepository.save(rating);
        Double newRatingUser = ratingRepository.getAvgScoreUser(rated.getUserId());
        if (!newRatingUser.equals(rated.getValoration())) {
            rated.setValoration(BigDecimal.valueOf(newRatingUser));
            rated.setValoration(BigDecimal.valueOf(rating.getRating()));
            userRepository.save(rated);
        }

        return new RatingDTO(rating);

    }

    public AvgRatingDTO calculateRating(Integer userId) {
        Double avg = ratingRepository.getAvgScoreUser(userId);
        return  new AvgRatingDTO(avg);
    }

}
