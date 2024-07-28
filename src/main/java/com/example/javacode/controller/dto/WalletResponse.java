package com.example.javacode.controller.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class WalletResponse {
    private UUID id;
    private BigDecimal balance;
}
