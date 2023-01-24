package com.binghe.inflearnspringaopexample.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @Pointcut에 포인트컷 표현식을 사용할 수 있다. (포인트컷을 변수화하는 것)
 * public으로해서 외부의 다른 Aspect에서도 포인트컷을 공유할 수 있다.
 */
@Slf4j
@Aspect
public class AspectV2 {

    @Pointcut("execution(* com.binghe.inflearnspringaopexample.order..*(..))")
    private void allOrder() {}

    // order 패키지와 하위 패키지
    @Around("allOrder()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니처
        return joinPoint.proceed();
    }
}
