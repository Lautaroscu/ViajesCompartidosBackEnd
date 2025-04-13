package com.viajes.viajesCompartidos.DTO.payments;

import com.viajes.viajesCompartidos.entities.payments.Recharge;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RechargeDTO implements Serializable {
    private Integer userId;
    private BigDecimal amount;
    private String paymentType;
    private LocalDateTime dateCreated;
    private String paymentMethod;

    public RechargeDTO(Recharge recharge) {
        this.userId = recharge.getUser().getUserId();
        this.amount = recharge.getAmount();
        this.paymentType = recharge.getPaymentMethod();
        this.dateCreated = recharge.getDateCreated();
    }
}
