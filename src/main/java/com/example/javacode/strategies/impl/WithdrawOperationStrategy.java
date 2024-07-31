package com.example.javacode.strategies.impl;

import com.example.javacode.dao.OperationType;
import com.example.javacode.dao.Transaction;
import com.example.javacode.dao.Wallet;
import com.example.javacode.exception.InsufficientFundsException;
import com.example.javacode.exception.WalletNotFoundException;
import com.example.javacode.repository.TransactionRepository;
import com.example.javacode.repository.WalletRepository;
import com.example.javacode.strategies.api.ChangeBalanceStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Component("WITHDRAW")
@RequiredArgsConstructor
public class WithdrawOperationStrategy implements ChangeBalanceStrategy {
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Retryable(
            value = {WalletNotFoundException.class},
            maxAttempts = 4,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    @Override
    public void changeBalance(UUID walletId, BigDecimal sum) {

        Wallet wallet = walletRepository.findByIdWithPessimisticLock(walletId).orElseThrow(WalletNotFoundException::new);
        if (wallet.getBalance().compareTo(sum) < 0) {
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
