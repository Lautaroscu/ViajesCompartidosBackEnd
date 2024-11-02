package com.viajes.viajesCompartidos.DTO.payments;

import com.viajes.viajesCompartidos.enums.PaymentMethodTypes;
import com.viajes.viajesCompartidos.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class RequestPayment implements Serializable {
    private PaymentMethodTypes paymentMethodType;
    private BigDecimal paymentAmount;
    private Integer userId;
    private Integer tripId;
    private String currency;
}
