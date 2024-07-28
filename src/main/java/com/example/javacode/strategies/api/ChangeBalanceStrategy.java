package com.example.javacode.strategies.api;

import com.example.javacode.dao.Wallet;
import com.example.javacode.dto.ChangeWalletBalanceRecord;

import java.math.BigDecimal;
import java.util.UUID;

public interface ChangeBalanceStrategy {
    void changeBalance(UUID walletId, BigDecimal sum);
}
