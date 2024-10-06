package com.viajes.viajesCompartidos.entities;

import com.viajes.viajesCompartidos.exceptions.trips.MaxPassengersOnBoardException;
import com.viajes.viajesCompartidos.exceptions.users.UserAlreadyExistsException;
import com.viajes.viajesCompartidos.exceptions.users.UserNotFoundException;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.viajes.ViajesCompartidos.entities.User;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "trip")
@Getter
@Setter
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

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false , referencedColumnName = "userId") // Relación con el dueño
    private com.viajes.ViajesCompartidos.entities.User owner;

    @ManyToMany
    @JoinTable(
            name = "trip_passengers", // Nombre de la tabla intermedia
            joinColumns = @JoinColumn(name = "trip_id" , referencedColumnName = "tripId"), // Columna que hace referencia a la entidad Trip
            inverseJoinColumns = @JoinColumn(name = "user_id" , referencedColumnName = "userId" ) // Columna que hace referencia a la entidad User
    )
    private List<User> passengers;

    public Trip() {}
    public Trip(String origin, String destination, LocalDateTime date, User owner , int maxPassengers) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.owner = owner;
        this.passengers = new ArrayList<>();
        this.maxPassengers = maxPassengers;
        this.countPassengers = 0;
    }




    public void addPassenger(User passenger) {
        if(passengers.contains(passenger)) {
            throw new UserAlreadyExistsException("The passenger is already on the trip");
        }
        if(countPassengers >= maxPassengers) {
            throw new MaxPassengersOnBoardException("Trip is full, cannot add more passengers");
        }
            this.passengers.add(passenger);
            countPassengers++;
    }
    public void removePassenger(User passenger){
        if(!passengers.contains(passenger)) {
            throw new UserNotFoundException("The passenger is not on the trip");
        }
        this.passengers.remove(passenger);
        countPassengers--;
    }
    }

