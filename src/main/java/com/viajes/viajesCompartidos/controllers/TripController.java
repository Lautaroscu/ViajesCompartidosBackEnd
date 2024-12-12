package com.viajes.viajesCompartidos.controllers;


import com.viajes.viajesCompartidos.DTO.OutputTripPassengerDTO;
import com.viajes.viajesCompartidos.DTO.TripPassengerDTO;
import com.viajes.viajesCompartidos.DTO.trip.CompleteTripDTO;
import com.viajes.viajesCompartidos.DTO.trip.FilterTripDTO;
import com.viajes.viajesCompartidos.DTO.trip.InputTripDTO;
import com.viajes.viajesCompartidos.DTO.trip.OutputTripDTO;
import com.viajes.viajesCompartidos.exceptions.BadRequestException;
import com.viajes.viajesCompartidos.exceptions.trips.InvalidLocationException;
import com.viajes.viajesCompartidos.exceptions.trips.MaxPassengersOnBoardException;
import com.viajes.viajesCompartidos.exceptions.trips.TripNotFoundException;
import com.viajes.viajesCompartidos.exceptions.users.UserAlreadyExistsException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
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
    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }
    @GetMapping
    public ResponseEntity<List<OutputTripDTO>> getTrips(
            @RequestParam(name = "origin" , required = false) String origin ,
            @RequestParam(name = "destination" , required = false) String destination ,
            @RequestParam(name = "userId" , required = false) Integer userId ,
            @RequestParam(name = "startDate" , required = false)LocalDateTime startDate ,
            @RequestParam(name = "endDate" , required = false)LocalDateTime endDate ,
            @RequestParam(name = "passengers" , required = false) Integer passengers ,
            @RequestParam(name = "sort" , required = false , defaultValue = "price") String sort ,
            @RequestParam(name = "order", required = false, defaultValue = "asc") String order // Parámetro de orden




    ) {
        FilterTripDTO filterTripDTO = new FilterTripDTO(origin, destination, passengers, userId,startDate , endDate);
        return ResponseEntity.status(HttpStatus.OK).body(tripService.findAll(filterTripDTO , sort , order));
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<OutputTripDTO> getTripById(@PathVariable int tripId) {
        OutputTripDTO outputTripDTO = tripService.findById(tripId);
        HttpStatus status = outputTripDTO != null ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(outputTripDTO);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OutputTripDTO>> getTripsByUserId(
            @PathVariable int userId,
            @RequestParam(required = false) String rol) {

        List<OutputTripDTO> trips;

        if ("owner".equalsIgnoreCase(rol)) {
            trips = tripService.getTripsByOwnerId(userId);
        } else if ("passenger".equalsIgnoreCase(rol)) {
            trips = tripService.getTripsByPassengerId(userId);
        } else {
            return ResponseEntity.badRequest().body(null); // Maneja el caso de un rol no válido
        }

        return ResponseEntity.ok(trips);
    }

    @PostMapping("/passengers")
    public ResponseEntity<?> addPassengerToTrip(@RequestBody TripPassengerDTO tripPassengerDTO) {
        try {
            OutputTripDTO tripPassengerDTO1 = tripService.addPassengerToTrip(tripPassengerDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(tripPassengerDTO1);
        } catch (UserAlreadyExistsException | MaxPassengersOnBoardException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (UserNotFoundException | TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred");
        }
    }



    @GetMapping("/passengers/{tripId}")
    public ResponseEntity<?> getPassengers(@PathVariable int tripId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(tripService.getPassengers(tripId));
        }catch (TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping
    public ResponseEntity<?> addTrip(@RequestBody InputTripDTO inputTripDTO) {
       try {
           return ResponseEntity.status(HttpStatus.CREATED).body(tripService.saveTrip(inputTripDTO));
       }catch (UserNotFoundException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found");
       }catch (BadRequestException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
       }
    }
    @PutMapping("/{tripId}")
    public ResponseEntity<?> updateTrip(@RequestBody InputTripDTO inputTripDTO , @PathVariable int tripId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(tripService.updateTrip(inputTripDTO, tripId));
        }catch (TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    @DeleteMapping("/{tripId}")
    public ResponseEntity<?> deleteTrip(@PathVariable int tripId) {
        try {
            tripService.deleteTrip(tripId);
            return  ResponseEntity.status(HttpStatus.OK).body("trip deleted successfully");
        }catch (TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PatchMapping("/cancel/{tripId}")
    public ResponseEntity<?> cancelTrip(@PathVariable int tripId) {
        try {
            tripService.cancelTrip(tripId);
            return ResponseEntity.status(HttpStatus.OK).body("Trip cancelled successfully");
        }catch (TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PatchMapping("/activate/{tripId}")
    public ResponseEntity<?> activateTrip(@PathVariable int tripId) {
        try {
            tripService.activateTrip(tripId);
            return ResponseEntity.status(HttpStatus.OK).body("Trip activated successfully");
        }catch (TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @DeleteMapping("/passengers/{tripId}/{userId}")
    public ResponseEntity<?> removePassengerFromTrip(@PathVariable int tripId, @PathVariable int userId) {
        try {
            tripService.removePassengerFromTrip(tripId , userId);
            return ResponseEntity.status(HttpStatus.OK).body("Passengers removed successfully");
        }catch (TripNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PatchMapping("/complete/{tripId}")
    public ResponseEntity<?> completeTrip(@RequestBody CompleteTripDTO completeTripDTO ,@PathVariable int tripId) {
        try {
            System.out.println(completeTripDTO);
            tripService.completeTrip(tripId , completeTripDTO);
            return ResponseEntity.ok().build();

        } catch (TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (InvalidLocationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }




}
