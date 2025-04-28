package com.viajes.viajesCompartidos.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TripAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ElementCollection
    private List<String> origin;

    @ElementCollection
    private List<String> destination;

    private LocalDateTime startDate; // o un rango

    private LocalDateTime endDate;


    private Integer maxPrice;

    private boolean active;

    private LocalDateTime createdAt;
}
