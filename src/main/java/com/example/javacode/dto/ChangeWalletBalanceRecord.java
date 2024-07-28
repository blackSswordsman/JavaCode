package com.example.javacode.dto;

import com.example.javacode.dao.OperationType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder
public record ChangeWalletBalanceRecord(UUID walletId, OperationType operationType, BigDecimal sum) {
}
