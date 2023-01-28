package com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CheckBalanceResult {

    private String bankName;
    private BigDecimal balance;

    public static CheckBalanceResult of(String bankName, BigDecimal balance) {
        return new CheckBalanceResult(bankName, balance);
    }
}
