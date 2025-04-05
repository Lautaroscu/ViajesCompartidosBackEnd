package com.viajes.viajesCompartidos.services.payments.TransactionStrategies;

import com.viajes.viajesCompartidos.DTO.wallet.TransactionDTO;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.entities.payments.Transaction;
import com.viajes.viajesCompartidos.entities.payments.Wallet;
import com.viajes.viajesCompartidos.enums.TransactionType;
import com.viajes.viajesCompartidos.repositories.UserRepository;
import com.viajes.viajesCompartidos.repositories.payments.TransactionRepository;
import com.viajes.viajesCompartidos.repositories.payments.WalletRepository;
import org.springframework.stereotype.Component;

@Component
public class RechargeTransactionStrategy extends BaseTransactionStrategy {

    protected RechargeTransactionStrategy(WalletRepository walletRepository, UserRepository userRepository, TransactionRepository transactionRepository) {
        super(walletRepository, userRepository, transactionRepository);
    }

    @Override
    public Transaction createTransaction(Integer userId ,TransactionDTO transactionDTO) {
        Transaction transaction = this.createBasicTransaction(transactionDTO);
        User user = getUserOrThrow(userId);
        Wallet wallet = user.getWallet();
        transaction.setWallet(wallet);
        if (!wallet.getId().equals(transactionDTO.getWalletId())) {
            throw new IllegalArgumentException("Wallet does not belong to the user");
        }
        wallet.setBalance(wallet.getBalance().add(transactionDTO.getAmount()));
        return transaction;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.RECHARGE;
    }
}
