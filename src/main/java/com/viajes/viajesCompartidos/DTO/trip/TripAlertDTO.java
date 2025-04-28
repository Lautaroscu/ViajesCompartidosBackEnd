package com.viajes.viajesCompartidos.DTO.trip;

import com.viajes.viajesCompartidos.entities.Trip;
import com.viajes.viajesCompartidos.entities.TripAlert;
import com.viajes.viajesCompartidos.entities.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TripAlertDTO implements Serializable {

    private Long id;


    private int userId;


    private List<String> origin;

    private List<String> destination;

    private LocalDateTime dateFrom;

    private LocalDateTime dateTo;


    private Integer maxPrice;

    private boolean active;

    private LocalDateTime createdAt;

    public TripAlertDTO(TripAlert tripAlert) {
        this.id = tripAlert.getId();
        this.userId = tripAlert.getUser().getUserId();
        this.origin = tripAlert.getOrigin();
        this.destination = tripAlert.getDestination();
        this.dateFrom = tripAlert.getStartDate();
        this.dateTo = tripAlert.getEndDate();
        this.maxPrice = tripAlert.getMaxPrice();
        this.active = tripAlert.isActive();
        this.createdAt = tripAlert.getCreatedAt();

    }
}
