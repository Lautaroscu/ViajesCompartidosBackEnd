package com.viajes.viajesCompartidos.services.payments.TransactionStrategies;

import com.viajes.viajesCompartidos.DTO.wallet.TransactionDTO;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.entities.payments.Transaction;
import com.viajes.viajesCompartidos.entities.payments.Wallet;
import com.viajes.viajesCompartidos.enums.TransactionType;
import com.viajes.viajesCompartidos.repositories.UserRepository;
import com.viajes.viajesCompartidos.repositories.payments.TransactionRepository;
import com.viajes.viajesCompartidos.repositories.payments.WalletRepository;
import com.viajes.viajesCompartidos.services.payments.TransactionStrategies.BaseTransactionStrategy;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransferTransactionStrategy extends BaseTransactionStrategy {

    @Autowired
    public TransferTransactionStrategy(WalletRepository walletRepository,
                                       UserRepository userRepository,
                                       TransactionRepository transactionRepository) {
        super(walletRepository, userRepository, transactionRepository);
    }

    @Override
    public Transaction createTransaction(Integer userId ,TransactionDTO transactionDTO) {
        User user = getUserOrThrow(userId);
        Transaction transaction = this.createBasicTransaction(transactionDTO);
        Wallet senderWallet = user.getWallet();

        if (senderWallet.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds for transfer");
        }

        // Restar del remitente
        senderWallet.setBalance(senderWallet.getBalance().subtract(transactionDTO.getAmount()));

        // Sumar al destinatario
        User recipientUser = getUserOrThrow(transactionDTO.getUserRecipient());
        Wallet recipientWallet = recipientUser.getWallet();

        if (recipientWallet == null) {
            throw new EntityNotFoundException("Recipient wallet not found");
        }

        recipientWallet.setBalance(recipientWallet.getBalance().add(transactionDTO.getAmount()));

        // Asociar al destinatario en la transacción
        transaction.setRecipient(recipientUser);

        senderWallet.addTransaction(transaction);

        return transaction;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.TRANSFER;
    }
}
