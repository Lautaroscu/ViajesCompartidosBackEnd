package com.viajes.viajesCompartidos.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String city; // Nombre de la ciudad

    @Column(nullable = false)
    private String exactPlace; // Lugar exacto (ej. "Terminal de buses")

    @Column(nullable = false)
    private double latitude; // Coordenadas

    @Column(nullable = false)
    private double longitude;

    public Location() {}

    public Location(String city, String exactPlace, double latitude, double longitude) {
        this.city = city;
        this.exactPlace = exactPlace;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
