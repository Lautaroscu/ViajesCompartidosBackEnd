package com.viajes.viajesCompartidos.repositories;

import com.viajes.viajesCompartidos.entities.TripAlert;
import com.viajes.viajesCompartidos.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TripAlertRepository extends JpaRepository<TripAlert, Long> {

    @Query(value = "SELECT t FROM TripAlert t WHERE t.user.userId = :userId")
    List<TripAlert> findAllByUserId(@Param("userId") int userId);

    List<TripAlert>  findAllByActiveTrueOrderByCreatedAtAsc();
}
