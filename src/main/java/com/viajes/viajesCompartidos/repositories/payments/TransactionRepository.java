package com.viajes.viajesCompartidos.repositories.payments;

import com.viajes.viajesCompartidos.entities.payments.Transaction;
import com.viajes.viajesCompartidos.enums.TransactionType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // Métodos de consulta personalizados si es necesario

    @Query(value = "SELECT t FROM Transaction t WHERE (t.wallet.user.userId = :userId AND (t.type IS NULL OR t.type = :type) )  ")
    List<Transaction> findByUserId(@Param("userId") Integer userId, @Param("type") TransactionType type , Pageable pageable);
}