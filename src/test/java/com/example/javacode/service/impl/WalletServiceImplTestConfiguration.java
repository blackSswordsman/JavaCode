package com.example.javacode.service.impl;

import com.example.javacode.dao.OperationType;
import com.example.javacode.repository.WalletRepository;
import com.example.javacode.strategies.api.ChangeBalanceStrategy;
import com.example.javacode.strategies.impl.DepositOperationStrategy;
import com.example.javacode.strategies.impl.WithdrawOperationStrategy;
import lombok.RequiredArgsConstructor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Primary;

import java.util.Map;

import static org.mockito.Mockito.mock;

@TestConfiguration
@RequiredArgsConstructor
public class WalletServiceImplTestConfiguration {

    private final WalletRepository walletRepository;

    @Bean
    @Primary
    public WalletServiceImpl walletServiceImplWithMockedMap() {
        Map<String, ChangeBalanceStrategy> operations = mock(Map.class);
        return new WalletServiceImpl(walletRepository,operations);
    }

}
