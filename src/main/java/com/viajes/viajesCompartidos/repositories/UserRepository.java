package com.viajes.viajesCompartidos.repositories;

import com.viajes.viajesCompartidos.entities.User;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT u FROM User u where u.email = :email")
    public Optional<User> findByEmail(@Param("email") String email);

    boolean existsByEmail(String email);
}
