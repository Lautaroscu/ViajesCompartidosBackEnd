package com.viajes.viajesCompartidos.entities.payments;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RechargeStatus status; // PENDING, COMPLETED, FAILED

    @Column(nullable = false)
    private LocalDateTime dateCreated;

    @Column(nullable = true)
    private String paymentMethod; // MP, tarjeta, transferencia, etc.

    // Constructor, getters y setters
    public Recharge() {
        dateCreated = LocalDateTime.now();
        status = RechargeStatus.PENDING;
    }
}
