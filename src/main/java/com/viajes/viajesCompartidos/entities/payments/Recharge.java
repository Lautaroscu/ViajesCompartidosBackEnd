package com.viajes.viajesCompartidos.entities.payments;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.enums.RechargeStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter

public class Recharge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private RechargeStatus status; // PENDING, COMPLETED, FAILED

    private LocalDateTime dateCreated;

    private String paymentMethod; // MP, tarjeta, transferencia, etc.

    // Constructor, getters y setters
    public Recharge() {
        dateCreated = LocalDateTime.now();
        status = RechargeStatus.PENDING;
        amount = BigDecimal.ZERO;
    }
}
