package com.viajes.viajesCompartidos.DTO.user;

import java.io.Serializable;
import java.math.BigDecimal;

public class BalanceDTO implements Serializable {
    private BigDecimal balance;
    public BalanceDTO() {}
    public BalanceDTO(BigDecimal balance) {
        this.balance = balance;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
