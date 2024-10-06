package com.viajes.viajesCompartidos.repositories;

import com.viajes.viajesCompartidos.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {


}
