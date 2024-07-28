package com.example.javacode.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Wallet not found")
public class WalletNotFoundException extends ApplicationException {

    public WalletNotFoundException() {
        super("Wallet not found");
    }
}
