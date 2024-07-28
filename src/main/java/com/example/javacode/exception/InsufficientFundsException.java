package com.example.javacode.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Insufficient Funds")
public class InsufficientFundsException extends ApplicationException {

    public InsufficientFundsException() {
        super("Insufficient Funds");
    }
}
