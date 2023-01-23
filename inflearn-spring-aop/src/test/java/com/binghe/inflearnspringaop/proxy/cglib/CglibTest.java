package com.binghe.inflearnspringaop.proxy.cglib;

import com.binghe.inflearnspringaop.proxy.cglib.code.TimeMethodInterceptor;
import com.binghe.inflearnspringaop.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CglibTest {

    @Test
    void clib() {
        ConcreteService target = new ConcreteService();

        // CGLIB은 Enhancer를 사용해서 프록시를 생성한다.
        Enhancer enhancer = new Enhancer();
        // CGLIB은 구체 클래스를 상속 받아서 프록시를 생성할 수 있다. 어떤 구체 클래스를 상속 받을지 지정한다.
        enhancer.setSuperclass(ConcreteService.class);
        // 프록시에 적용할 실행 로직을 할당한다.
        enhancer.setCallback(new TimeMethodInterceptor(target));
        // 프록시를 생성한다.
        ConcreteService proxy = (ConcreteService) enhancer.create();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();
    }
}
