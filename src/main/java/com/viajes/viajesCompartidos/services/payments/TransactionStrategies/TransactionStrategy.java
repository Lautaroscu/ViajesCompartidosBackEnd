package com.viajes.viajesCompartidos.services.payments.TransactionStrategies;

import com.viajes.viajesCompartidos.DTO.wallet.TransactionDTO;
import com.viajes.viajesCompartidos.entities.payments.Transaction;
import com.viajes.viajesCompartidos.enums.TransactionType;

public interface TransactionStrategy {
    Transaction createTransaction(Integer userId ,TransactionDTO transactionDTO);
    TransactionType getTransactionType();
}
