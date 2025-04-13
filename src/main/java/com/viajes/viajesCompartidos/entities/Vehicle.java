package com.viajes.viajesCompartidos.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Vehicle {
    @Id
    private String plate;
    private String brand;
    private String model;
    private String color;
    private int year;
    private int seatingCapacity;
    private boolean available;
    private boolean predetermined;


    @ManyToOne
    @JoinColumn(name = "owner_id")
    @JsonBackReference
    private User owner;





}
