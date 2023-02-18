package com.binghe.threadperrequestmodelperformanceimprovesynchronous.infrastructure.v3;

import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.CheckBalance;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.CheckBalanceResult;
import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class KakaokBankCheckBalanceV3 implements CheckBalance {
    @StopWatch
    @Override
    public CheckBalanceResult checkBalance(Long memberId) {
        try {
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("API 호출중 예외 발생");
    }
}
