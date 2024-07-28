package com.example.javacode.strategies.impl;

import com.example.javacode.dao.OperationType;
import com.example.javacode.dao.Transaction;
import com.example.javacode.dao.Wallet;
import com.example.javacode.exception.InsufficientFundsException;
import com.example.javacode.exception.WalletNotFoundException;
import com.example.javacode.repository.TransactionRepository;
import com.example.javacode.repository.WalletRepository;
import com.example.javacode.strategies.api.ChangeBalanceStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component("WITHDRAW")
@RequiredArgsConstructor
public class WithdrawOperationStrategy implements ChangeBalanceStrategy {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    @Override
    public void changeBalance(UUID walletId, BigDecimal sum) {

        Wallet wallet = walletRepository.findById(walletId).orElseThrow(WalletNotFoundException::new);
        if(wallet.getBalance().compareTo(sum) < 0) {
            throw new InsufficientFundsException();
        }
        wallet.setBalance(wallet.getBalance().subtract(sum));

        Transaction transaction = new Transaction()
                .setOperationType(OperationType.DEPOSIT)
                .setSum(sum)
                .setWallet(wallet);
        transactionRepository.save(transaction);
    }
}
