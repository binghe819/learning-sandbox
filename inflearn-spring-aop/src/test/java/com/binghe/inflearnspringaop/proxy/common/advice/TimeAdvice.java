package com.binghe.inflearnspringaop.proxy.common.advice;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 스프링에서 제공하는 Advice.
 * Target이 없어도 재활용이 가능하다.
 */
@Slf4j
public class TimeAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

//        Object result = methodProxy.invoke(target, args);
        // invocation.proceed()는 알아서 Target을 찾아서 실행해준다.
        Object result = invocation.proceed();

        long endTime = System.currentTimeMillis();
        log.info("TimeProxy 종료 - 소요시간 = {result}", endTime - startTime);

        return result;
    }
}
