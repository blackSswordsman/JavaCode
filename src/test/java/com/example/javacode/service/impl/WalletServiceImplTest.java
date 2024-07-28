package com.example.javacode.service.impl;

import com.example.javacode.dao.OperationType;
import com.example.javacode.dao.Wallet;
import com.example.javacode.dto.ChangeWalletBalanceRecord;
import com.example.javacode.exception.OperationTypeNotSupportedException;
import com.example.javacode.exception.WalletNotFoundException;
import com.example.javacode.repository.WalletRepository;
import com.example.javacode.strategies.api.ChangeBalanceStrategy;
import com.example.javacode.strategies.impl.DepositOperationStrategy;
import org.apache.el.util.ReflectionUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.ReflectionUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ContextConfiguration(classes = {WalletServiceImplTestConfiguration.class})
class WalletServiceImplTest {
    @Autowired
    private WalletServiceImpl sut;

    @Autowired
    private WalletRepository walletRepository;


    @Test
    void given_idAndWalletWithThisIdExistsInDB_when_getWalletInvoked_then_returnsThisWallet() {
        // given
        Wallet wallet = walletRepository.save(new Wallet().setBalance(BigDecimal.valueOf(500)));
        UUID id = wallet.getId();

        // when
        Wallet actualResult = sut.getWallet(id);
        // then
        assertNotNull(actualResult);
        assertEquals(wallet.getId(), actualResult.getId());
        assertEquals(0, BigDecimal.valueOf(500).compareTo(actualResult.getBalance()));
    }

    @Test
    void given_idAndWalletWithThisIdDoesNotExistsInDB_when_getWalletInvoked_then_throwsWalletNotFoundException() {
        // given
        UUID id = UUID.randomUUID();
        // when then
        assertThrows(WalletNotFoundException.class, () -> sut.getWallet(id));
    }

    @Test
    void given_ChangeWalletBalanceRecord_when_ChangeBalanceStrategyInvoked_then_returnsUpdatedWallet() {
        // given
        UUID uuid = walletRepository.save(new Wallet().setBalance(BigDecimal.ZERO)).getId();
        ChangeWalletBalanceRecord changeWalletBalanceRecord = ChangeWalletBalanceRecord.builder()
                .walletId(uuid)
                .operationType(OperationType.DEPOSIT)
                .sum(BigDecimal.valueOf(500))
                .build();
        when(sut.changeBalanceStrategyMap.containsKey(eq(changeWalletBalanceRecord.operationType().name())))
                .thenReturn(true);
        when(sut.changeBalanceStrategyMap.get(eq(changeWalletBalanceRecord.operationType().name())))
                .thenReturn((id, sum) -> {});
        // when
        Wallet actualResult = sut.changeBalance(changeWalletBalanceRecord);
        // then
        assertNotNull(actualResult);
        assertEquals(changeWalletBalanceRecord.walletId(), actualResult.getId());
        assertEquals(0, BigDecimal.ZERO.compareTo(actualResult.getBalance()));
    }

    @Test
    void given_ChangeWalletBalanceRecord_when_ChangeBalanceStrategyInvoked_then_throwsWalletNotFoundException() {
        // given
        ChangeWalletBalanceRecord changeWalletBalanceRecord = ChangeWalletBalanceRecord.builder()
                .walletId(UUID.randomUUID())
                .operationType(OperationType.DEPOSIT)
                .sum(BigDecimal.valueOf(500))
                .build();
        when(sut.changeBalanceStrategyMap.containsKey(eq(changeWalletBalanceRecord.operationType().name())))
                .thenReturn(true);
        // when then
        assertThrows(WalletNotFoundException.class, () -> sut.changeBalance(changeWalletBalanceRecord));
    }

    @Test
    void given_ChangeWalletBalanceRecord_when_ChangeBalanceStrategyInvoked_then_throwsOperationNotSupportedException() {
        // given
        ChangeWalletBalanceRecord changeWalletBalanceRecord = ChangeWalletBalanceRecord.builder()
                .walletId(UUID.randomUUID())
                .operationType(OperationType.DEPOSIT)
                .sum(BigDecimal.valueOf(500))
                .build();
        when(sut.changeBalanceStrategyMap.containsKey(eq(changeWalletBalanceRecord.operationType().name())))
                .thenReturn(false);
        // when then
        assertThrows(OperationTypeNotSupportedException.class, () -> sut.changeBalance(changeWalletBalanceRecord));
    }


    @Test
    void when_createNewWalletInvoked_then_returnsNewWallet() {
        // when
        Wallet actualResult = sut.createNewWallet();
        // then
        assertNotNull(actualResult);
        assertNotNull(actualResult.getId());
        assertEquals(BigDecimal.ZERO, actualResult.getBalance());
    }

}