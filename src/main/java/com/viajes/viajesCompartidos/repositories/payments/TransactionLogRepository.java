package com.viajes.viajesCompartidos.repositories.payments;

import com.viajes.viajesCompartidos.entities.payments.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
    // Métodos de consulta personalizados si es necesario
}