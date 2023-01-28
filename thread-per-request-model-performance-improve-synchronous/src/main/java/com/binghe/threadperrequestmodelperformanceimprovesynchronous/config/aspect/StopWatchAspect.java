package com.binghe.threadperrequestmodelperformanceimprovesynchronous.config.aspect;

import com.binghe.threadperrequestmodelperformanceimprovesynchronous.domain.StopWatch;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class StopWatchAspect {

    @Around("execution(* *(..)) && @annotation(timeCount)")
    public Object doTransaction(ProceedingJoinPoint joinPoint, StopWatch timeCount) throws Throwable {
        // 호출된 타겟 클래스명 및 메서드명.
        String taskName = joinPoint.getSignature().toShortString();
        org.springframework.util.StopWatch stopWatch = new org.springframework.util.StopWatch();
        try {
            stopWatch.start(taskName);
            log.info("{} started! ", taskName);
            Object result = joinPoint.proceed();
            stopWatch.stop();
            log.info("{} latency : {} ms", stopWatch.getLastTaskName(), stopWatch.getTotalTimeMillis());
            return result;
        } catch (Exception e) {
            stopWatch.stop();
            log.info("{} exception latency : {} ms", stopWatch.getLastTaskName(), stopWatch.getTotalTimeMillis());
            throw e;
        }
    }
}
