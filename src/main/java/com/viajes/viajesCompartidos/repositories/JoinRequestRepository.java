package com.viajes.viajesCompartidos.repositories;

import com.viajes.viajesCompartidos.entities.JoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface JoinRequestRepository extends JpaRepository<JoinRequest, Long> {
    List<JoinRequest> findByTrip_TripId(Integer tripId);
    List<JoinRequest> findByUser_UserId(Integer userId);

    boolean existsByTrip_TripId(Integer tripId);
    boolean existsByUser_UserId(Integer userId);

    Optional<JoinRequest> findByTrip_TripIdAndUser_UserId(Integer tripId, Integer userId);

    void deleteByTrip_TripId(Integer tripId);

}
