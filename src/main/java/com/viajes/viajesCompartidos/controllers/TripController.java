package com.viajes.viajesCompartidos.controllers;


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
            @RequestParam(name = "maxPrice" , required = false) Double maxPrice,
            @RequestParam(name = "sort", required = false, defaultValue = "price") String sort,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order , // Parámetro de orden
            @RequestParam(name = "tripType" , required = false) TripType tripType ,
            @RequestParam(name= "strict" , required = false , defaultValue = "true") String strict


            ) {
        FilterTripDTO filterTripDTO = new FilterTripDTO(origin, destination, passengers, userId, startDate, endDate , maxPrice , tripType , strict);
        return ResponseEntity.status(HttpStatus.OK).body(tripService.findAll(filterTripDTO, sort, order));
    }
    @PostMapping("/passengers")
    public ResponseEntity<?> addPassengerToTrip(@RequestBody TripPassengerDTO tripPassengerDTO) {
        try {
            OutputTripPassengerDTO tripPassengerDTO1 = tripService.addPassengerToTrip(tripPassengerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(tripPassengerDTO1);
        } catch (UserAlreadyExistsException | MaxPassengersOnBoardException | NotEnoughBalanceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (UserNotFoundException | TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<OutputTripDTO> getTripById(@PathVariable int tripId) {
        OutputTripDTO outputTripDTO = tripService.findById(tripId);
        HttpStatus status = outputTripDTO != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(outputTripDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTripsByUserId(
            @PathVariable int userId,
            @RequestParam(required = false) String rol ,
            @RequestParam(required = false) TripStatus status
    )
    {
        try {
            //si no hay params, entonces devolvemos todos los viajes en los que aparece ese userId
            //ya sea driver o passenger
            List<OutputTripPreviewDTO> trips = (rol == null || rol.isEmpty()) && status == null ? tripService.getTripsOfUser(userId) : tripService.getTripByUserIdRolAndStatus(userId ,rol , status);
            return ResponseEntity.ok(trips);

        }catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/passengers/{tripId}")
    public ResponseEntity<?> getPassengers(@PathVariable int tripId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(tripService.getPassengers(tripId));
        } catch (TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> addTrip(@RequestBody InputTripDTO inputTripDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(tripService.saveTrip(inputTripDTO));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<?> updateTrip(@RequestBody InputTripDTO inputTripDTO, @PathVariable int tripId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(tripService.updateTrip(inputTripDTO, tripId));
        } catch (TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<?> deleteTrip(@PathVariable int tripId) {
        try {
            tripService.deleteTrip(tripId);
            return ResponseEntity.status(HttpStatus.OK).body("trip deleted successfully");
        } catch (TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (TripContainsPassangersException t) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(t.getMessage());
        }
    }

    @PatchMapping("/cancel/{tripId}")
    public ResponseEntity<?> cancelTrip(@PathVariable int tripId) {
        try {
            tripService.cancelTrip(tripId);
            return ResponseEntity.status(HttpStatus.OK).body("Trip cancelled successfully");
        } catch (TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/activate/{tripId}")
    public ResponseEntity<?> activateTrip(@PathVariable int tripId) {
        try {
            tripService.activateTrip(tripId);
            return ResponseEntity.status(HttpStatus.OK).body("Trip activated successfully");
        } catch (TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/passengers/{tripId}/{userId}")
    public ResponseEntity<?> removePassengerFromTrip(@PathVariable int tripId, @PathVariable int userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(tripService.removePassengerFromTrip(tripId, userId));
        } catch (TripNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/complete/{tripId}")
    public ResponseEntity<?> completeTrip(@PathVariable Integer tripId, @RequestBody CompleteTripDTO completeTripDTO) {
        try {
            tripService.completeTrip(tripId, completeTripDTO);
            return ResponseEntity.ok().build();

        } catch (TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (InvalidLocationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }


    @GetMapping("/{tripId}/chat")
    public ResponseEntity<?> getChat(@PathVariable int tripId) {
        return ResponseEntity.status(HttpStatus.OK).body(chatService.getChat(tripId));
    }


}
