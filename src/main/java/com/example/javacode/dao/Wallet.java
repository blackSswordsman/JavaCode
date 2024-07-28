package com.example.javacode.dao;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "wallet")
@Accessors(chain = true)
@Entity
public class Wallet extends Identifiable{
    @OneToMany(fetch = FetchType.LAZY)
    private Set<Transaction> transactions;

    @Column(name = "balance")
    private BigDecimal balance;

    public Wallet setId(UUID id) {
        super.id=id;
        return this;
    }

}
