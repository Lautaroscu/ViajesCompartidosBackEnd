package com.viajes.viajesCompartidos.DTO.wallet;

import com.viajes.viajesCompartidos.entities.payments.Transaction;
import com.viajes.viajesCompartidos.entities.payments.Wallet;
import com.viajes.viajesCompartidos.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long walletId;
    private int userId;
    private BigDecimal balance;
    private BigDecimal totalExpenses;

    public WalletDTO(Wallet wallet) {
        this.walletId = wallet.getId();
        this.userId = wallet.getUser().getUserId();
        this.balance = wallet.getBalance();
        this.totalExpenses = wallet.getTransactions().stream().filter(transaction -> transaction.getType().equals(TransactionType.EXPENSE)).map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

    }
}
