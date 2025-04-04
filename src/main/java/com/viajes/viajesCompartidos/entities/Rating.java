package com.viajes.viajesCompartidos.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private int rating;

    @JoinColumn(name = "trip_id" , referencedColumnName = "tripId")
    @OneToOne
    private Trip trip;

    @JoinColumn(name = "user_rater_id" )
    @OneToOne
    private User userRater;

    @JoinColumn(name =  "user_rated_id")
    @OneToOne
    private User userRated;



}
