package com.viajes.viajesCompartidos.entities.payments;

import com.viajes.viajesCompartidos.enums.PaymentMethodTypes;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethodTypes paymentMethodType; // Tipo de método de pago

    private String provider; // Ejemplo: "VISA", "MasterCard"
    private Boolean isActive; // Para activar o desactivar métodos específicos

    // Constructor vacío
    public PaymentMethod() {}

    // Constructor con parámetros
    public PaymentMethod(PaymentMethodTypes paymentMethodType, String provider, Boolean isActive) {
        this.paymentMethodType = paymentMethodType;
        this.provider = provider;
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "PaymentMethod{" +
                "id=" + id +
                ", paymentMethodType=" + paymentMethodType +
                ", provider='" + provider + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
