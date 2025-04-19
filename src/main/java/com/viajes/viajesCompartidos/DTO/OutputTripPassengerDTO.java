package com.viajes.viajesCompartidos.DTO;
import com.viajes.viajesCompartidos.DTO.location.OutputLocationDTO;
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
    private int userId;
    private BigDecimal userValoration;
    private Long walletId;
    private int tripId;
    private OutputLocationDTO origin;
    private OutputLocationDTO destination;
    private LocalDateTime departureTime;
    private Double price;
    public OutputTripPassengerDTO(User user, Trip trip) {
        this.userName = user.getFirstName();
        this.userLastName = user.getLastName();
        this.walletId = user.getWallet().getId();
        this.origin = new OutputLocationDTO(trip.getOrigin());
        this.destination = new OutputLocationDTO(trip.getDestination());
        this.tripId = trip.getTripId();
        this.departureTime = trip.getDate();
        this.price = trip.getPrice();
        this.userId = user.getUserId();
        this.userValoration = user.getValoration();
    }
}
