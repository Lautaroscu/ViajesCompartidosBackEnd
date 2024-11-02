package com.viajes.viajesCompartidos.services.payments;

import com.viajes.viajesCompartidos.repositories.payments.TransactionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionLogService {
    private final TransactionLogRepository transactionLogRepository;
    @Autowired
    public TransactionLogService(TransactionLogRepository transactionLogRepository) {
        this.transactionLogRepository = transactionLogRepository;
    }


}
