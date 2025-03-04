package com.viajes.viajesCompartidos.entities;

import com.viajes.viajesCompartidos.enums.TripStatus;
import com.viajes.viajesCompartidos.exceptions.trips.MaxPassengersOnBoardException;
import com.viajes.viajesCompartidos.exceptions.users.NotEnoughBalanceException;
import com.viajes.viajesCompartidos.exceptions.users.UserAlreadyExistsException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.viajes.viajesCompartidos.entities.User;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "trip")
@Data
@Check(name = "" , constraints = "max_passengers >= count_passengers")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tripId;
    @Column(nullable = false)
    private String origin;
    @Column(nullable = false)
    private String destination;
    @Column
    private LocalDateTime date;
    @Column(name = "max_passengers")
    private int maxPassengers;
    @Column(name = "count_passengers")
    private int countPassengers;
    @Column(name = "price")
    private double price;
    @Column(name = "comment")
    private String comment;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false , referencedColumnName = "userId") // Relación con el dueño
    private User owner;
    @Enumerated(EnumType.STRING)
    private TripStatus status;

    @ManyToMany
    @JoinTable(
            name = "trip_passengers", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "trip_id" , referencedColumnName = "tripId"), // Columna que hace referencia a la entidad Trip
            inverseJoinColumns = @JoinColumn(name = "user_id" , referencedColumnName = "userId" ) // Columna que hace referencia a la entidad User
    )
    private List<User> passengers;
    @Column
    private boolean isPrivate;


    @OneToOne(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private Chat chat;

    public Trip() {}
    public Trip(String origin, String destination, LocalDateTime date, User owner , int maxPassengers ,double price , String comment , boolean isPrivate ) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.owner = owner;
        this.passengers = new ArrayList<>();
        this.maxPassengers = maxPassengers;
        this.countPassengers = 0;
        this.price = price;
        this.comment = comment;
        this.status = TripStatus.PENDING;
        this.isPrivate = isPrivate;
    }




    public void addPassenger(User passenger) {
        if(passenger.getBalance().doubleValue() < this.getPrice()){
            throw new NotEnoughBalanceException("Not enough balance");
        }
        if(passengers.contains(passenger)) {
            throw new UserAlreadyExistsException("The passenger is already on the trip");
        }
        if(countPassengers >= maxPassengers) {
            throw new MaxPassengersOnBoardException("Trip is full, cannot add more passengers");
        }
            this.passengers.add(passenger);
            countPassengers++;
            if(countPassengers >=1){
                    setStatus(TripStatus.ACTIVE);
            }
    }
    public void removePassenger(User passenger){
        if(!passengers.contains(passenger)) {
            throw new UserNotFoundException("The passenger is not on the trip");
        }
        this.passengers.remove(passenger);
        countPassengers--;
        if(countPassengers < 1){
            setStatus(TripStatus.PENDING);
        }
    }

   public void setTripState(TripStatus status) {
        this.status = status;
   }


    }

