package com.binghe.inflearnspringaopexample.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * 어드바이스 종류는 @Around 말고도 여러가지 종류가 있다.
 * - @Around: 메서드 호출 전후에 수행. 가장 강력한 어드바이스, 조인 포인트 실행 여부 선택, 반환값 변환, 예외 변환등 가능
 * - @Before: 조인 포인트 실행 이전에 실행
 * - @AfterReturning: 조인 포인트가 정상 완료후 실행
 * - @AfterThrowing: 메서드가 예외를 던지는 경우 실행
 * - @After: 조인 포인트가 정상 또는 예외에 관계없이 실행 (finally)
 *
 * 모두 @Around가 할 수 있는 일의 일부만 제공할 뿐이다. 따라서 @Around 어드바이스만 사용해도 필요한 기능을 모두 수행할 수 있다.
 * 단, 역할을 딱 지정하기위해서 각각의 애노테이션을 사용한다. (콜백과 같은 역할)
 */
@Slf4j
@Aspect
public class AspectV5 {

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

    /**
     * 조인 포인트 실행 전 (프록시 실행이후 Target 실행 전)
     * @Around는 `joinPoint.proceed()`를 눌러줘야 Target이 실행되는 반면, @Before은 자동으로 다음 Target이 호출된다.
     * 물론, 예외 발생시 다음 Target이 실행되지 않는다.
     */
    @Before("com.binghe.inflearnspringaopexample.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[before] {}", joinPoint.getSignature());
    }

    /**
     * 메서드 실행이 정상적으로 반환될 때 실행. (Target 실행후 정상적으로 반환될 때 실행)
     */
    @AfterReturning(value = "com.binghe.inflearnspringaopexample.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[return] {} return={}", joinPoint.getSignature(), result);
    }

    /**
     * 메서드 실행이 예외를 던져서 종료될 때 실행 (Target 실행도중 예외 발생시 실행)
     */
    @AfterThrowing(value = "com.binghe.inflearnspringaopexample.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[ex] {} message={}", joinPoint.getSignature(), ex.getMessage());
    }

    /**
     * 메서드 실행이 종료되면 실행된다. (finally 역할이며, Target 실행 완료후 실행.)
     */
    @After(value = "com.binghe.inflearnspringaopexample.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {}", joinPoint.getSignature());
    }
}
