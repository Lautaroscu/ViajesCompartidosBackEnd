package com.viajes.viajesCompartidos.repositories;

import com.viajes.viajesCompartidos.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
}
