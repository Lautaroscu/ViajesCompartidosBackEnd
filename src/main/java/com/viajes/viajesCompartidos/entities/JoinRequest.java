package com.viajes.viajesCompartidos.entities;

import com.viajes.viajesCompartidos.enums.RequestStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Setter
public class JoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // El usuario que solicita unirse.

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip; // El viaje al que se solicita unirse.

    @Enumerated(EnumType.STRING)
    private RequestStatus status; // Estado de la solicitud (e.g., PENDING, ACCEPTED, REJECTED).

    private LocalDateTime requestDate; // Fecha de la solicitud.

    private String message;


    public JoinRequest() {
        status = RequestStatus.PENDING;
        requestDate = LocalDateTime.now();
    }
    public JoinRequest(User user, Trip trip, RequestStatus status , String message) {
        this.user = user;
        this.trip = trip;
        this.status = status;
        this.requestDate = LocalDateTime.now();
        this.message = message;
    }



}
