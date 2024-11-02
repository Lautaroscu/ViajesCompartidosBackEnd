package com.viajes.viajesCompartidos.repositories.payments;

import com.viajes.viajesCompartidos.entities.payments.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    // Métodos de consulta personalizados si es necesario
}