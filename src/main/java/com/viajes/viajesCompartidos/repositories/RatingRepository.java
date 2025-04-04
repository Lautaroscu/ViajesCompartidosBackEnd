package com.viajes.viajesCompartidos.repositories;

import com.viajes.viajesCompartidos.entities.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RatingRepository extends JpaRepository<Rating, Integer> {
    @Query(value = "SELECT AVG(r.rating) FROM Rating r WHERE r.userRated.userId = :userId")
    Double getAvgScoreUser(@Param("userId") int userId);
}
