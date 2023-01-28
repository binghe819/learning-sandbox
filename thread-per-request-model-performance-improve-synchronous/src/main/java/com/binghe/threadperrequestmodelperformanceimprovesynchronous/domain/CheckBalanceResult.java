package com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CheckBalanceResult {
    public static final CheckBalanceResult EMPTY = new CheckBalanceResult("", BigDecimal.ZERO);

    private String bankName;
    private BigDecimal balance;

    public static CheckBalanceResult of(String bankName, BigDecimal balance) {
        return new CheckBalanceResult(bankName, balance);
    }
}
