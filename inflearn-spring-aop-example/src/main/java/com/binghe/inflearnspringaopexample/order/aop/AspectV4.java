package com.binghe.inflearnspringaopexample.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

/**
 * 애스펙트별 순서를 적용하려면 @Order를 사용하면 된다.
 * 이때 주의할 점은 하나의 애스펙트 클래스에 여러 어드바이스가 있으면 순서를 보장 받을 수 없다.
 * 띠라서 애스펙트를 별도의 클래스로 분리해줘야한다.
 */
@Slf4j
public class AspectV4 {

    @Aspect
    @Order(2)
    public static class LogAspect {
        @Around("com.binghe.inflearnspringaopexample.order.aop.Pointcuts.allOrder()")
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[log] {}", joinPoint.getSignature());
            return joinPoint.proceed();
        }
    }

    @Aspect
    @Order(1)
    public static class TxAspect {
        @Around("com.binghe.inflearnspringaopexample.order.aop.Pointcuts.orderAndService()")
        public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
            try {
                log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
                Object result = joinPoint.proceed();
                log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
                return result;
            } catch (Exception e) {
                log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
                throw e;
            } finally {
                log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
            }
        }
    }
}
