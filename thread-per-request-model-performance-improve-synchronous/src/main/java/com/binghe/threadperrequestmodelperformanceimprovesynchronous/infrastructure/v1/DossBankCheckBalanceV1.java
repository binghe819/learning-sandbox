package com.binghe.threadperrequestmodelperformanceimprovesynchronous.infrastructure.v1;

import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.CheckBalance;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.CheckBalanceResult;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.infrastructure.timecount.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class DossBankCheckBalanceV1 implements CheckBalance {
    private static final String BANK_NAME = "DOSS";

    private final ExecutorService executorService;

    public DossBankCheckBalanceV1() {
        this.executorService = Executors.newFixedThreadPool(10);
    }

    @StopWatch
    @Override
    public CheckBalanceResult checkBalance(Long memberId) {

        // DossBank로부터 HTTP 요청 -> Sleep으로 대체.
        try {
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int randomBalance = (int) (Math.random() * 10_000) + 1_000;

        return CheckBalanceResult.of(BANK_NAME, BigDecimal.valueOf(randomBalance));
    }
}
