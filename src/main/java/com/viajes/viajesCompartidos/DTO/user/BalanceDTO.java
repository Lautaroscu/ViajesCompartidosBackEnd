package com.viajes.viajesCompartidos.DTO.user;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
@Data
public class BalanceDTO implements Serializable {
    private BigDecimal amount;
    public BalanceDTO() {}
    public BalanceDTO(BigDecimal balance) {
        this.amount = balance;
    }
    public BigDecimal getBalance() {
        return amount;
    }
    public void setBalance(BigDecimal balance) {
        this.amount = balance;
    }
}
