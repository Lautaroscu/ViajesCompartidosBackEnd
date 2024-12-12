package com.viajes.viajesCompartidos.DTO.payments;

import com.viajes.viajesCompartidos.entities.payments.Payment;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class ResponsePayment implements Serializable {
    private BigDecimal paymentAmount;
    private Integer userId;
    private Integer tripId;
    private String currency;

    public ResponsePayment(Payment payment) {
        paymentAmount = payment.getAmount();
        userId = payment.getUser().getUserId();
        tripId = payment.getTrip().getTripId();
        currency = payment.getCurrency();
    }
}
