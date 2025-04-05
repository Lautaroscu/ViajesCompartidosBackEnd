package com.viajes.viajesCompartidos.services.payments;

import com.viajes.viajesCompartidos.repositories.payments.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    private final TransactionRepository transactionLogRepository;
    @Autowired
    public TransactionService(TransactionRepository transactionLogRepository) {
        this.transactionLogRepository = transactionLogRepository;
    }


}
