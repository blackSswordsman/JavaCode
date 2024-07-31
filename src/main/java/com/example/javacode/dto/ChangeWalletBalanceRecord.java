package com.example.javacode.dto;

import com.example.javacode.dao.OperationType;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;


public record ChangeWalletBalanceRecord(UUID walletId, OperationType operationType, BigDecimal sum) {

    public static ChangeWalletBalanceRecordBuilder builder(){
        return new ChangeWalletBalanceRecordBuilder();
    }
    public static class ChangeWalletBalanceRecordBuilder {
        private UUID walletId;
        private OperationType operationType;
        private BigDecimal sum;

        public ChangeWalletBalanceRecordBuilder walletId(UUID walletId) {
            this.walletId = walletId;
            return this;
        }

        public ChangeWalletBalanceRecordBuilder operationType(OperationType operationType) {
            this.operationType = operationType;
            return this;
        }

        public ChangeWalletBalanceRecordBuilder sum(BigDecimal sum) {
            if(sum.compareTo(BigDecimal.ZERO)<0){
                throw new IllegalArgumentException("sum must be positive");
            }
            this.sum = sum;
            return this;
        }

        public ChangeWalletBalanceRecord build() {
            if(walletId==null){
                throw new IllegalArgumentException("walletId must be set");
            }
            if(operationType==null){
                throw new IllegalArgumentException("operationType must be set");
            }
            if(sum==null){
                throw new IllegalArgumentException("sum must be set");
            }
            return new ChangeWalletBalanceRecord(walletId,operationType,sum);
        }
    }
}
