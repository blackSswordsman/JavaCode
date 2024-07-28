package com.example.javacode.service.api;

import com.example.javacode.dao.Transaction;

import java.util.Optional;

public interface TransactionService {
    Optional<Transaction> findTransactionById(int id);

}
