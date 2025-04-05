package com.viajes.viajesCompartidos.entities.payments;

import com.viajes.viajesCompartidos.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal balance;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;

    public Wallet() {
        this.transactions = new ArrayList<>();
        this.balance = BigDecimal.ZERO;
    }
    public Wallet(BigDecimal balance, User user) {
        this.balance = balance;
        this.user = user;
    }
    public Wallet(User user) {
        this.user = user;
        this.balance = BigDecimal.ZERO;

    }

    // Getters, setters, agregar/quitar transacción, etc.
    public void addTransaction(Transaction transaction) {
        transaction.setWallet(this);
        this.transactions.add(transaction);
    }
    public void removeTransaction(Transaction transaction) {
        transaction.setWallet(null);
        this.transactions.remove(transaction);
    }
}
