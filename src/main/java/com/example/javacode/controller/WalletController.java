package com.example.javacode.controller;

import com.example.javacode.controller.dto.ChangeWalletBalanceRequest;
import com.example.javacode.controller.dto.WalletResponse;
import com.example.javacode.dao.Wallet;
import com.example.javacode.dto.ChangeWalletBalanceRecord;
import com.example.javacode.service.api.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
public class WalletController {
    private final WalletService walletService;
    @PostMapping("/wallet")
    public WalletResponse changeWalletBalance(@Valid @RequestBody ChangeWalletBalanceRequest changeWalletBalanceRequest) {
        ChangeWalletBalanceRecord record = new ChangeWalletBalanceRecord(changeWalletBalanceRequest.getWalletId(),
                changeWalletBalanceRequest.getOperationType(),changeWalletBalanceRequest.getSum());
        Wallet wallet = walletService.changeBalance(record);
        return getWalletResponse(wallet);
    }

    @GetMapping("/wallets/{id}")
    public WalletResponse getWallet(@PathVariable UUID id) {
        Wallet wallet = walletService.getWallet(id);
        return getWalletResponse(wallet);
    }
    @PostMapping("/create_wallet")
    public WalletResponse createNewWallet(){
        Wallet wallet = walletService.createNewWallet();
        return getWalletResponse(wallet);
    }

    private static WalletResponse getWalletResponse(Wallet wallet) {
        return new WalletResponse()
                .setId(wallet.getId())
                .setBalance(wallet.getBalance());
    }
}
