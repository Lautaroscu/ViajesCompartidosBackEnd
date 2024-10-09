package com.viajes.viajesCompartidos.services;


import com.viajes.viajesCompartidos.DTO.OutputTripPassengerDTO;
import com.viajes.viajesCompartidos.DTO.TripPassengerDTO;
import com.viajes.viajesCompartidos.DTO.trip.InputTripDTO;
import com.viajes.viajesCompartidos.DTO.trip.OutputTripDTO;
import com.viajes.viajesCompartidos.DTO.user.OutputUserDTO;
import com.viajes.viajesCompartidos.entities.Trip;

import com.viajes.viajesCompartidos.exceptions.BadRequestException;
import com.viajes.viajesCompartidos.exceptions.trips.TripNotFoundException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import com.viajes.viajesCompartidos.repositories.TripRepository;
import com.viajes.viajesCompartidos.repositories.UserRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import com.viajes.viajesCompartidos.entities.User;

@Service
public class TripService {
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private UserRepository userRepository;

    public List<OutputTripDTO> findAll() {
        return tripRepository
                .findAll()
                .stream()
                .map(OutputTripDTO::new)
                .toList();


    }
    public OutputTripDTO addPassengerToTrip(TripPassengerDTO tripPassengerDTO) {
        Trip trip = tripRepository.findById(tripPassengerDTO.getTripID())
                .orElseThrow(() -> new TripNotFoundException("Trip not found"));
        User user = userRepository.findById(tripPassengerDTO.getPassengerID())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Intenta agregar el pasajero; si falla, se lanzará una excepción específica
        trip.addPassenger(user);

        // Persistir los cambios en la base de datos
        tripRepository.save(trip);
        return new OutputTripDTO(trip);
    }



    public OutputTripDTO findById(int id) {
        return tripRepository.findById(id).map(OutputTripDTO::new).orElse(null);

    }
    public OutputTripDTO saveTrip(InputTripDTO trip) {
        User owner = userRepository.findById(trip.getOwnerId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        if(isBadRequest(trip)) {
            throw new BadRequestException("Bad Request , check the fields and try again");
        }
        Trip newTrip = new Trip(trip.getOrigin() , trip.getDestination() , trip.getDate() , owner , trip.getMaxPassengers());
        tripRepository.save(newTrip);
        return new OutputTripDTO(newTrip);

    }
    public OutputTripDTO updateTrip(InputTripDTO trip , int tripId) {

        Trip tripUpdated = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        if(isBadRequest(trip)){
            throw new BadRequestException("Bad Request , check the fields and try again");
        }
        tripUpdated.setOrigin(trip.getOrigin());
        tripUpdated.setDestination(trip.getDestination());
        tripUpdated.setDate(trip.getDate());
        if(trip.getMaxPassengers() < tripUpdated.getCountPassengers())
            throw new IllegalArgumentException("Max passengers could not be lower than the count of passengers on board");
        tripUpdated.setMaxPassengers(trip.getMaxPassengers());


        tripUpdated = tripRepository.save(tripUpdated);

        return new OutputTripDTO(tripUpdated);
    }
    public void deleteTrip(int id) {
        if(!tripRepository.existsById(id)){
            throw new TripNotFoundException("Trip not found");
        }
        tripRepository.deleteById(id);
    }

    private boolean isBadRequest(InputTripDTO trip) {
        return trip.getOrigin() == null || trip.getDestination() == null || trip.getDate() == null || trip.getMaxPassengers() == 0;
    }

    public List<OutputUserDTO> getPassengers(int tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        return trip
                .getPassengers()
                .stream()
                .map(OutputUserDTO::new)
                .toList();
    }

    public void removePassengerFromTrip(int tripId, int userId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException("Trip not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        trip.removePassenger(user);
        tripRepository.save(trip);

    }


    public List<OutputTripDTO> getTripsByOwnerId(int userId) {
        return tripRepository
                .findByOwner_UserId(userId)
                .stream()
                .map(OutputTripDTO::new)
                .toList();
    }

    public List<OutputTripDTO> getTripsByPassengerId(int userId) {
        return tripRepository
                .findByPassengers_UserId(userId)
                .stream()
                .map(OutputTripDTO::new)
                .toList();
    }
}
