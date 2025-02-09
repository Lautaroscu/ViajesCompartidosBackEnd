package com.viajes.viajesCompartidos.entities;

import com.viajes.viajesCompartidos.enums.RequestStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
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


    public JoinRequest() {
        status = RequestStatus.PENDING;
        requestDate = LocalDateTime.now();
    }
    public JoinRequest(User user, Trip trip, RequestStatus status) {
        this.user = user;
        this.trip = trip;
        this.status = status;
        this.requestDate = LocalDateTime.now();
    }
    // Getters y setters


    public Trip getTrip() {
        return trip;
    }
    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }
    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public Long getId() {
        return id;
    }

    public RequestStatus getStatus() {
        return status;
    }
    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
