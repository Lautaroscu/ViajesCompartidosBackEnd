package com.viajes.viajesCompartidos.controllers;

import com.viajes.viajesCompartidos.DTO.rating.RatingDTO;
import com.viajes.viajesCompartidos.exceptions.trips.TripNotFoundException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import com.viajes.viajesCompartidos.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratings")
public class RatingController {


    private final RatingService ratingService;
    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/rate")
    public ResponseEntity<?> rateUser(@RequestBody RatingDTO ratingDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(ratingService.save(ratingDTO));
        }catch (UserNotFoundException userNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFoundException.getMessage());
        }catch (TripNotFoundException tripNotFoundException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(tripNotFoundException.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @GetMapping("/userId/{userId}")
    public ResponseEntity<?> getUserRating(@PathVariable Integer userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ratingService.calculateRating(userId));
        }catch (UserNotFoundException userNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(userNotFoundException.getMessage());
        }
    }
}
