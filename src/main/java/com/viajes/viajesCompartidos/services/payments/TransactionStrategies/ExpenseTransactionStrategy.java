package com.viajes.viajesCompartidos.services.payments.TransactionStrategies;

import com.viajes.viajesCompartidos.DTO.wallet.TransactionDTO;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.entities.payments.Transaction;
import com.viajes.viajesCompartidos.entities.payments.Wallet;
import com.viajes.viajesCompartidos.enums.TransactionType;
import com.viajes.viajesCompartidos.repositories.UserRepository;
import com.viajes.viajesCompartidos.repositories.payments.TransactionRepository;
import com.viajes.viajesCompartidos.repositories.payments.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExpenseTransactionStrategy extends BaseTransactionStrategy {

    @Autowired
    public ExpenseTransactionStrategy(WalletRepository walletRepository,
                                      UserRepository userRepository,
                                      TransactionRepository transactionRepository) {
        super(walletRepository, userRepository, transactionRepository);
    }

    @Override
    public Transaction createTransaction(Integer userId ,TransactionDTO transactionDTO) {
        User user = getUserOrThrow(userId);
        Transaction transaction = this.createBasicTransaction(transactionDTO);
        Wallet wallet = user.getWallet();
        if (!wallet.getId().equals(transactionDTO.getWalletId())) {
            throw new IllegalArgumentException("Wallet does not belong to the user");
        }

        if (wallet.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance for expense");
        }

        wallet.setBalance(wallet.getBalance().subtract(transactionDTO.getAmount()));
        wallet.addTransaction(transaction);
        return transaction;
    }


    @Override
    public TransactionType getTransactionType() {
        return TransactionType.EXPENSE;
    }
}
