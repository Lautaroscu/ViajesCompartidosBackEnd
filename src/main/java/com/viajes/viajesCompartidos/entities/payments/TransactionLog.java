package com.viajes.viajesCompartidos.entities.payments;

import com.viajes.viajesCompartidos.enums.PaymentStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class TransactionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private LocalDateTime timestamp;
    private String message; // Mensaje del evento o error
    private PaymentStatus status; // Estado asociado al evento (PENDING, COMPLETED, etc.)
}
