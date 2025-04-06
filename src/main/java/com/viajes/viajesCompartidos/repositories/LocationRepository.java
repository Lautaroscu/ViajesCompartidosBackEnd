package com.viajes.viajesCompartidos.repositories;

import com.viajes.viajesCompartidos.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Integer> {
    public Optional<Location> findByCity(String city);

    Optional<Location> findByCityAndExactPlace(String cityName, String exactPlace);
}
