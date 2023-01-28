package com.binghe.threadperrequestmodelperformanceimprovesynchronous.infrastructure.v1;

import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.CheckBalance;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.CheckBalanceResult;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class ShinhenBankCheckBalanceV1 implements CheckBalance {
    private static final String BANK_NAME = "SHINHEN";

    private final ExecutorService executorService;

    public ShinhenBankCheckBalanceV1() {
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @StopWatch
    @Override
    public CheckBalanceResult checkBalance(Long memberId) {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int randomBalance = (int) (Math.random() * 10_000) + 1_000;

        return CheckBalanceResult.of(BANK_NAME, BigDecimal.valueOf(randomBalance));
    }
}
