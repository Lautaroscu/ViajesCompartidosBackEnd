package com.viajes.viajesCompartidos.DTO;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.entities.Trip;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OutputTripPassengerDTO implements Serializable {
    private String userName;
    private  String userLastName;
    private BigDecimal userBalance;
    private int tripId;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private Double price;
    public OutputTripPassengerDTO(User user, Trip trip) {
        this.userName = user.getFirstName();
        this.userLastName = user.getLastName();
        this.userBalance = user.getBalance();
        this.origin = trip.getOrigin();
        this.destination = trip.getDestination();
        this.tripId = trip.getTripId();
        this.departureTime = trip.getDate();
        this.price = trip.getPrice();
    }
}
