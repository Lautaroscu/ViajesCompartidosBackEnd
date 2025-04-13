package com.viajes.viajesCompartidos.services.payments;

import com.viajes.viajesCompartidos.DTO.wallet.TransactionDTO;
import com.viajes.viajesCompartidos.DTO.wallet.WalletDTO;
import com.viajes.viajesCompartidos.entities.User;
import com.viajes.viajesCompartidos.entities.payments.Transaction;
import com.viajes.viajesCompartidos.entities.payments.Wallet;
import com.viajes.viajesCompartidos.enums.TransactionType;
import com.viajes.viajesCompartidos.exceptions.EntityNotFoundException;
import com.viajes.viajesCompartidos.repositories.UserRepository;
import com.viajes.viajesCompartidos.repositories.payments.TransactionRepository;
import com.viajes.viajesCompartidos.repositories.payments.WalletRepository;
import com.viajes.viajesCompartidos.services.payments.TransactionStrategies.TransactionStrategy;
import com.viajes.viajesCompartidos.services.payments.TransactionStrategies.TransactionStrategyFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionStrategyFactory transactionStrategyFactory;

    public WalletService(WalletRepository walletRepository, UserRepository userRepository, TransactionRepository transactionRepository, TransactionStrategyFactory transactionStrategyFactory) {
        this.walletRepository = walletRepository;
        this.transactionStrategyFactory = transactionStrategyFactory;
        this.transactionRepository = transactionRepository;
    }

    public TransactionDTO addTransaction(Integer userId, TransactionDTO transactionDTO) {
        TransactionStrategy transactionStrategy = transactionStrategyFactory.getStrategy(transactionDTO.getTransactionType());
       Transaction transaction= transactionStrategy.createTransaction(userId ,transactionDTO);
       Wallet wallet = transaction.getWallet();
       walletRepository.save(wallet);

       return new TransactionDTO(transaction);
    }
    public List<TransactionDTO> getTopXTransactionsByType(Integer userId , TransactionType transactionType , Integer topX) {
        PageRequest pageRequest = PageRequest.of(0, topX, Sort.by("timestamp").descending());

        List<TransactionDTO> t = transactionRepository.findByUserId(userId , transactionType ,  pageRequest)
                .stream()
                .map(TransactionDTO::new)
                .toList();
        System.out.println(t);
        return t;
    }

    public WalletDTO getWallet(Integer userId) {
        Wallet wallet = walletRepository.findByUserId(userId).orElseThrow(()->new EntityNotFoundException("Wallet not found"));
        return new WalletDTO(wallet);
    }

    public void updateBalance(Integer userId, BigDecimal amount) {


        Wallet wallet = walletRepository.findByUserId(userId).orElseThrow(()->new EntityNotFoundException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
    }
}
