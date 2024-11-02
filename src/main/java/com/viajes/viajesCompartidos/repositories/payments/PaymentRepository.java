package com.viajes.viajesCompartidos.repositories.payments;

import com.viajes.viajesCompartidos.entities.payments.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Puedes agregar métodos de consulta personalizados si es necesario
}