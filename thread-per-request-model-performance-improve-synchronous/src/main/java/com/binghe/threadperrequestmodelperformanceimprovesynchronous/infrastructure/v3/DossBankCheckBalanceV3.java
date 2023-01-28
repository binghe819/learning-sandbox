package com.binghe.threadperrequestmodelperformanceimprovesynchronous.infrastructure.v3;

import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.CheckBalance;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.CheckBalanceResult;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class DossBankCheckBalanceV3 implements CheckBalance {
    private static final String BANK_NAME = "DOSS";

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
