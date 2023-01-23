package com.binghe.inflearnspringaop.proxy.cglib.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {

    private final Object target;

    public TimeMethodInterceptor(Object target) {
        this.target = target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        // MethodInterceptor에선 Method보다 MethodProxy를 사용하는 것이 성능상 더 좋다고한다.
        Object result = methodProxy.invoke(target, args);

        long endTime = System.currentTimeMillis();
        log.info("TimeProxy 종료 - 소요시간 = {result}", endTime - startTime);

        return result;
    }
}
