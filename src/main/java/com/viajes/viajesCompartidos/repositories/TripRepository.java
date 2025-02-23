package com.viajes.viajesCompartidos.repositories;

import com.viajes.viajesCompartidos.entities.Trip;
import com.viajes.viajesCompartidos.enums.TripStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> , JpaSpecificationExecutor<Trip> {

    @Query("SELECT t FROM Trip t JOIN t.passengers p WHERE p.userId = :passengerId and t.status = :status")
    List<Trip> findByPassengers_UserId(@Param("passengerId") int passengerId , @Param("status") TripStatus status);





}
