package com.viajes.viajesCompartidos.repositories;

import com.viajes.viajesCompartidos.entities.Chat;
import com.viajes.viajesCompartidos.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {
    @Query(value = "SELECT c FROM Chat c where c.trip.tripId = :tripId")
    Optional<Chat> findByTripId(@Param("tripId") int tripId);
}
