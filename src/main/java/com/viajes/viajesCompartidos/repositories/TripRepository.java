package com.viajes.viajesCompartidos.repositories;

import com.viajes.viajesCompartidos.entities.Trip;
import com.viajes.viajesCompartidos.enums.TripStatus;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> , JpaSpecificationExecutor<Trip> {

    @Query("SELECT t FROM Trip t  JOIN t.passengers p WHERE p.userId = :passengerId")
    List<Trip> findByPassengers_UserId(@Param("passengerId") int passengerId);

    @Query("SELECT t from  Trip t LEFT JOIN t.passengers p  WHERE t.owner.userId = :userId OR p.userId = :userId")
    List<Trip> findTripsOfUser(@Param("userId") Integer userId);
}
