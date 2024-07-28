package com.example.javacode.controller.dto;

import com.example.javacode.dao.OperationType;
import jakarta.annotation.Nonnull;
import lombok.Data;
import lombok.NonNull;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ChangeWalletBalanceRequest {
    @NotNull(message = "wallet id must be not null")
    private UUID walletId;
    @NotNull(message = "operation type must be not null")
    private OperationType operationType;
    @NotNull(message = "sum must be not null")
    @Positive(message = "sum must be positive")
    private BigDecimal sum;
}
