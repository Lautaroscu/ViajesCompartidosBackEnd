package com.viajes.viajesCompartidos.controllers;


import com.viajes.viajesCompartidos.DTO.ErrorResponse;
import com.viajes.viajesCompartidos.DTO.OutputTripPassengerDTO;
import com.viajes.viajesCompartidos.DTO.TripPassengerDTO;
import com.viajes.viajesCompartidos.DTO.trip.*;
import com.viajes.viajesCompartidos.enums.TripStatus;
import com.viajes.viajesCompartidos.enums.TripType;
import com.viajes.viajesCompartidos.exceptions.BadRequestException;
import com.viajes.viajesCompartidos.exceptions.location.InvalidLocationException;
import com.viajes.viajesCompartidos.exceptions.trips.MaxPassengersOnBoardException;
import com.viajes.viajesCompartidos.exceptions.trips.TripContainsPassangersException;
import com.viajes.viajesCompartidos.exceptions.trips.TripNotFoundException;
import com.viajes.viajesCompartidos.exceptions.users.NotEnoughBalanceException;
import com.viajes.viajesCompartidos.exceptions.users.UserAlreadyExistsException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import com.viajes.viajesCompartidos.services.ChatService;
import com.viajes.viajesCompartidos.services.TripService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trips")

public class TripController {
    private final TripService tripService;
    private final ChatService chatService;

    @Autowired
    public TripController(TripService tripService, ChatService chatService) {
        this.tripService = tripService;
        this.chatService = chatService;
    }

    @GetMapping
    public ResponseEntity<List<OutputTripPreviewDTO>> getTrips(
            @RequestParam(name = "origin", required = false) String origin,
            @RequestParam(name = "destination", required = false) String destination,
            @RequestParam(name = "userId", required = false) Integer userId,
            @RequestParam(name = "startDate", required = false) LocalDateTime startDate,
            @RequestParam(name = "endDate", required = false) LocalDateTime endDate,
            @RequestParam(name = "passengers", required = false) Integer passengers,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "sort", required = false, defaultValue = "price") String sort,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order, // Parámetro de orden
            @RequestParam(name = "tripType", required = false) TripType tripType,
            @RequestParam(name = "strict", required = false, defaultValue = "true") String strict


    ) {
        FilterTripDTO filterTripDTO = new FilterTripDTO(origin, destination, passengers, userId, startDate, endDate, maxPrice, tripType, strict);
        return ResponseEntity.status(HttpStatus.OK).body(tripService.findAll(filterTripDTO, sort, order));
    }

    @PostMapping("/passengers")
    public ResponseEntity<?> addPassengerToTrip(@RequestBody TripPassengerDTO tripPassengerDTO) {
        OutputTripPassengerDTO tripPassengerDTO1 = tripService.addPassengerToTrip(tripPassengerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(tripPassengerDTO1);

    }

    @GetMapping("/{tripId}")
    public ResponseEntity<OutputTripDTO> getTripById(@PathVariable int tripId) {

        return ResponseEntity.status(HttpStatus.OK).body(tripService.findById(tripId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTripsByUserId(
            @PathVariable int userId,
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) TripStatus status
    ) {

        List<OutputTripPreviewDTO> trips = (rol == null || rol.isEmpty()) && status == null ? tripService.getTripsOfUser(userId) : tripService.getTripByUserIdRolAndStatus(userId, rol, status);
        return ResponseEntity.ok(trips);

    }


    @GetMapping("/passengers/{tripId}")
    public ResponseEntity<?> getPassengers(@PathVariable int tripId) {

        return ResponseEntity.status(HttpStatus.OK).body(tripService.getPassengers(tripId));

    }

    @PostMapping
    public ResponseEntity<?> addTrip(@RequestBody InputTripDTO inputTripDTO) {

        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.saveTrip(inputTripDTO));

    }

    @PutMapping("/{tripId}")
    public ResponseEntity<?> updateTrip(@RequestBody InputTripDTO inputTripDTO, @PathVariable int tripId) {

        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.updateTrip(inputTripDTO, tripId));

    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<?> deleteTrip(@PathVariable int tripId) {
        return ResponseEntity.status(HttpStatus.OK).body(tripService.deleteTrip(tripId));
    }

    @PatchMapping("/cancel/{tripId}")
    public ResponseEntity<?> cancelTrip(@PathVariable int tripId) {

        return ResponseEntity.status(HttpStatus.OK).body(tripService.cancelTrip(tripId));

    }

    @PatchMapping("/activate/{tripId}")
    public ResponseEntity<?> activateTrip(@PathVariable int tripId) {

        return ResponseEntity.status(HttpStatus.OK).body(tripService.activateTrip(tripId));
    }

    @DeleteMapping("/passengers/{tripId}/{userId}")
    public ResponseEntity<?> removePassengerFromTrip(@PathVariable int tripId, @PathVariable int userId) {

        return ResponseEntity.status(HttpStatus.OK).body(tripService.removePassengerFromTrip(tripId, userId));

    }

    @PatchMapping("/complete/{tripId}")
    public ResponseEntity<?> completeTrip(@PathVariable Integer tripId, @RequestBody CompleteTripDTO completeTripDTO) {

        return ResponseEntity.status(HttpStatus.OK).body(tripService.completeTrip(tripId, completeTripDTO));

    }


    @GetMapping("/{tripId}/chat")
    public ResponseEntity<?> getChat(@PathVariable int tripId) {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.getChat(tripId));
    }

    @PostMapping("/alerts")
    public ResponseEntity<TripAlertDTO> createTripAlert(@RequestBody TripAlertDTO tripAlertDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.createTripAlert(tripAlertDTO));
    }

    @GetMapping("/alerts/userId/{userid}")
    public ResponseEntity<List<TripAlertDTO>> getAllTripAlertsOfUser(@PathVariable int userid) {
        return ResponseEntity.ok(tripService.getAllTripsAlertsOfUser(userid));
    }


}
