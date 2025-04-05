package com.viajes.viajesCompartidos.repositories.payments;

import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.entities.payments.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    @Query(value = "SELECT w FROM Wallet w WHERE w.user.userId = :userId")
    Optional<Wallet> findByUserId(@Param("userId") Integer userId);
}
