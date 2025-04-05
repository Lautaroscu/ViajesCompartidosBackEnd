package com.viajes.viajesCompartidos.services.payments.TransactionStrategies;

import com.viajes.viajesCompartidos.enums.TransactionType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TransactionStrategyFactory {

    private final Map<TransactionType, TransactionStrategy> strategies = new HashMap<>();

    public TransactionStrategyFactory(List<TransactionStrategy> strategyList) {
        for (TransactionStrategy strategy : strategyList) {
            strategies.put(strategy.getTransactionType(), strategy);
        }
    }

    public TransactionStrategy getStrategy(TransactionType type) {
        TransactionStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported transaction type: " + type);
        }
        return strategy;
    }
}
