package com.viajes.viajesCompartidos.DTO.trip;

import com.viajes.viajesCompartidos.entities.Trip;
import com.viajes.viajesCompartidos.enums.TripStatus;
import com.viajes.viajesCompartidos.enums.TripType;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OutputTripPreviewDTO implements Serializable {
    private Integer tripId;
    private String originCityName;
    private String destinationCityName;
    private Double price;
    private Integer maxPassengers;
    private Integer countOfPassengers;
    private LocalDateTime date;
    private String ownerName;
    private BigDecimal ownerValoration;
    private TripType tripType;
    private boolean hasChat;
    private TripStatus tripStatus;

    public OutputTripPreviewDTO(Trip trip) {
        this.tripId = trip.getTripId();
        originCityName = trip.getOrigin().getCity();
        destinationCityName = trip.getDestination().getCity();
        price = trip.getPrice();
        maxPassengers = trip.getMaxPassengers();
        countOfPassengers = trip.getCountPassengers();
        this.date = trip.getDate();
        ownerName = trip.getOwner().getFirstName() + " " + trip.getOwner().getLastName();
        this.tripType = trip.getTripType();
        hasChat = trip.getChat() != null;
        ownerValoration = trip.getOwner().getValoration();
        tripStatus = trip.getStatus();

    }

}
