package com.example.javacode.service.impl;

import com.example.javacode.dao.OperationType;
import com.example.javacode.dao.Wallet;
import com.example.javacode.dto.ChangeWalletBalanceRecord;
import com.example.javacode.exception.OperationTypeNotSupportedException;
import com.example.javacode.exception.WalletNotFoundException;
import com.example.javacode.repository.WalletRepository;
import com.example.javacode.service.api.WalletService;
import com.example.javacode.strategies.api.ChangeBalanceStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    final Map<String, ChangeBalanceStrategy> changeBalanceStrategyMap;

    @Override
    public Wallet getWallet(UUID id) {
        return walletRepository.findById(id).orElseThrow(WalletNotFoundException::new);
    }

    @Override
    public Wallet changeBalance(ChangeWalletBalanceRecord changeWalletBalanceRecord) {
        if(!changeBalanceStrategyMap.containsKey(changeWalletBalanceRecord.operationType().name())){
            throw new OperationTypeNotSupportedException();
        }
        if(!walletRepository.existsById(changeWalletBalanceRecord.walletId())) {
            throw new WalletNotFoundException();
        }
        changeBalanceStrategyMap.get(changeWalletBalanceRecord.operationType().name())
                .changeBalance(changeWalletBalanceRecord.walletId(), changeWalletBalanceRecord.sum());
        return getWallet(changeWalletBalanceRecord.walletId());
    }

    @Override
    public Wallet createNewWallet() {
        Wallet wallet = new Wallet()
                .setBalance(BigDecimal.ZERO);
        wallet=walletRepository.save(wallet);
        return wallet;
    }

}
