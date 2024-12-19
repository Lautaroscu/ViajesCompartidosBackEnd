package com.viajes.viajesCompartidos.DTO.payments;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
public class RequestPayment implements Serializable {
    private BigDecimal paymentAmount;
    private Integer userId;
    private String userEmail;
    private Integer tripId;
    private String currency;
}
