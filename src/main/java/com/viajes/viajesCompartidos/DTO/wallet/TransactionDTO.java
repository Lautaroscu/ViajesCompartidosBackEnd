package com.viajes.viajesCompartidos.DTO.wallet;

import com.viajes.viajesCompartidos.entities.payments.Transaction;
import com.viajes.viajesCompartidos.enums.TransactionStatus;
import com.viajes.viajesCompartidos.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long transactionId;
    private Long walletId;
    private BigDecimal amount;
    private String description;
    private TransactionType transactionType;
    private Integer userRecipient;
    private LocalDateTime timestamp;
    private TransactionStatus status;

    public TransactionDTO(Transaction transaction) {
        this.transactionId = transaction.getId();
        this.walletId = transaction.getWallet().getId();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();
        this.transactionType = transaction.getType();
        this.timestamp = transaction.getTimestamp();
        if(transaction.getRecipient() != null)  {
            this.userRecipient = transaction.getRecipient().getUserId();

        }
        this.status = transaction.getStatus();

    }
}
