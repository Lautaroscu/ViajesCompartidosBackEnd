package com.viajes.viajesCompartidos.entities.payments;

import com.viajes.viajesCompartidos.entities.Trip;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Payment {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String currency;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;


    @ManyToOne // Relación con PaymentMethod
    @JoinColumn(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethodDetails; // Detalles del método de pago

    public Payment(Long id , User payer , Trip trip , BigDecimal amount , String currency , PaymentMethod paymentMethodDetails) {
        this.user = payer;
        this.trip = trip;
        this.amount = amount;
        this.currency = currency;
        this.createdAt = LocalDateTime.now();
        this.status = PaymentStatus.PENDING;
        this.paymentMethodDetails = paymentMethodDetails;
        this.id = id;
    }

    public Payment() {

    }

    public boolean isSuccessful() {
        return status == PaymentStatus .APPROVED;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", user=" + user +
                ", trip=" + trip +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", createdAt=" + createdAt +
                ", status=" + status +
                ", paymentMethodDetails=" + paymentMethodDetails +
                '}';
    }
}