package com.viajes.viajesCompartidos.repositories.payments;

import com.viajes.viajesCompartidos.entities.payments.Refund;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefundRepository extends JpaRepository<Refund, Long> {
    // Métodos de consulta personalizados si es necesario
}