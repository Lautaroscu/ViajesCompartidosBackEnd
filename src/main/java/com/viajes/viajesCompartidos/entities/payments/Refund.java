package com.viajes.viajesCompartidos.entities.payments;

import com.viajes.viajesCompartidos.enums.RefundStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private Payment payment;

    private LocalDateTime requestDate;
    private LocalDateTime processedDate;
    private BigDecimal amount;
    private RefundStatus status; // PENDING, APPROVED, REJECTED, etc.

}
