package com.viajes.viajesCompartidos.repositories;

import com.viajes.viajesCompartidos.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    List<Trip> findByOwner_UserId(int ownerId);

    @Query("SELECT t FROM Trip t JOIN t.passengers p WHERE p.userId = :passengerId")
    List<Trip> findByPassengers_UserId(@Param("passengerId") int passengerId);

}
