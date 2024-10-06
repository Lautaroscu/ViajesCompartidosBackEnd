package com.viajes.viajesCompartidos.repositories;

import com.viajes.ViajesCompartidos.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
