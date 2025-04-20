package com.viajes.viajesCompartidos.controllers;

import com.viajes.viajesCompartidos.DTO.JoinRequestDTO;
import com.viajes.viajesCompartidos.DTO.RequestStatusDTO;
import com.viajes.viajesCompartidos.entities.JoinRequest;
import com.viajes.viajesCompartidos.enums.RequestStatus;
import com.viajes.viajesCompartidos.exceptions.JoinRequestNotFoundException;
import com.viajes.viajesCompartidos.exceptions.trips.TripNotFoundException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import com.viajes.viajesCompartidos.services.JoinRequestService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/join-requests")
public class JoinRequestController {

    private final JoinRequestService joinRequestService;

    @Autowired
    public JoinRequestController(JoinRequestService joinRequestService) {
        this.joinRequestService = joinRequestService;
    }

    @PostMapping
    public ResponseEntity<?> sendJoinRequest(@RequestBody JoinRequestDTO joinRequestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(joinRequestService.sendJoinRequest(joinRequestDTO));
        } catch (TripNotFoundException | UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (MessagingException m) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(m.getMessage());
        }
    }


    @GetMapping("/trip/{tripId}")
    public ResponseEntity<?> getRequestsForTrip(@PathVariable Integer tripId) {
        try {
            List<JoinRequestDTO> requests = joinRequestService.getRequestsForTrip(tripId);
            return ResponseEntity.status(HttpStatus.OK).body(requests);

        } catch (TripNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/requestId/{id}")
    public ResponseEntity<?> setRequestStatus(@PathVariable Long id, @RequestBody RequestStatusDTO status) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(joinRequestService.updateRequestStatus(id, status));
        } catch (JoinRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/request/trip/{tripId}/user/{userId}")
    public ResponseEntity<?> getRequestByTripAndUser(@PathVariable Integer tripId, @PathVariable Integer userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(joinRequestService.getByTripAndUser(tripId, userId));

        } catch (JoinRequestNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}

