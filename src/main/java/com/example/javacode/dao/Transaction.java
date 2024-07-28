package com.example.javacode.dao;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "transaction")
@Entity
@Accessors(chain = true)
public class Transaction extends Identifiable {

    @Column(name = "operation_type")
    private OperationType operationType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(name = "sum")
    private BigDecimal sum;
}
