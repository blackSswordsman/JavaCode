package com.example.javacode.service.api;


import com.example.javacode.dao.Wallet;
import com.example.javacode.dto.ChangeWalletBalanceRecord;

import java.util.UUID;

public interface WalletService {
    Wallet getWallet(UUID id);
    Wallet changeBalance(ChangeWalletBalanceRecord changeWalletBalanceRecord);
    Wallet createNewWallet();

}