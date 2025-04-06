package com.viajes.viajesCompartidos.entities.payments;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.enums.TransactionStatus;
import com.viajes.viajesCompartidos.enums.TransactionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @ManyToOne
    @JoinColumn(name = "wallet_id", nullable = false)
    @JsonBackReference
    private Wallet wallet;

    private String description; // Opcional, por si querés agregar algo como "Unión a viaje #42"

    // En caso de transferencias, podrías guardar el destinatario (otro usuario)
    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    // Getters y setters
    public Transaction() {}
    public Transaction(BigDecimal amount, TransactionType type, Wallet wallet, String description, User recipient) {
        this.amount = amount;
        this.type = type;
        this.wallet = wallet;
        this.description = description;
        this.recipient = recipient;
        this.timestamp = LocalDateTime.now();

    }

}
