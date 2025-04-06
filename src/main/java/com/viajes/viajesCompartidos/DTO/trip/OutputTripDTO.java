package com.viajes.viajesCompartidos.DTO.trip;

import com.viajes.viajesCompartidos.DTO.chat.ChatDTO;
import com.viajes.viajesCompartidos.DTO.location.OutputLocationDTO;
import com.viajes.viajesCompartidos.DTO.user.UserOwnerDTO;
import com.viajes.viajesCompartidos.DTO.user.UserPassengerDTO;
import com.viajes.viajesCompartidos.entities.Trip;
import com.viajes.viajesCompartidos.enums.TripStatus;
import com.viajes.viajesCompartidos.enums.TripType;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OutputTripDTO  implements Serializable  {
    private int tripId;
    private OutputLocationDTO origin;
    private OutputLocationDTO destination;
    private List<UserPassengerDTO> passengers;
    private UserOwnerDTO owner;
    private int maxPassengers;
    private  int countOfPassengers;
    private TripStatus trip_status;
    private TripType tripType;
    private Double price;
    private LocalDateTime date;

    private ChatDTO chat;

    public OutputTripDTO()  {}
    public OutputTripDTO(Trip trip) {
       this.tripId = trip.getTripId();
       this.origin= new OutputLocationDTO(trip.getOrigin());
       this.destination = new OutputLocationDTO(trip.getDestination());
       this.passengers = trip.getPassengers().stream().map(UserPassengerDTO::new).collect(Collectors.toList());
       this.owner = new UserOwnerDTO(trip.getOwner());
       this.maxPassengers = trip.getMaxPassengers();
       this.countOfPassengers = trip.getPassengers().size();
       this.trip_status = trip.getStatus();
       this.tripType = trip.getTripType();
       this.chat = new ChatDTO(trip.getChat());
       this.date = trip.getDate();
       this.price = trip.getPrice();

    }


    public void setChat(ChatDTO chat) {
        this.chat = chat;
    }
}
