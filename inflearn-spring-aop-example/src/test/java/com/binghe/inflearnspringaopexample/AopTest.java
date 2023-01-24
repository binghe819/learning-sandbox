package com.binghe.inflearnspringaopexample;

import com.binghe.inflearnspringaopexample.order.OrderRepository;
import com.binghe.inflearnspringaopexample.order.OrderService;
import com.binghe.inflearnspringaopexample.order.aop.AspectV1;
import com.binghe.inflearnspringaopexample.order.aop.AspectV2;
import com.binghe.inflearnspringaopexample.order.aop.AspectV3;
import com.binghe.inflearnspringaopexample.order.aop.AspectV4;
import com.binghe.inflearnspringaopexample.order.aop.AspectV5;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * @Aspect는 애스펙트라는 표식이지 컴포넌트 스캔의 대상은 아니다. 그러므로 직접 넣어줘야한다.
 *
 * 실제 프로젝트에선 @Component로 스캔 대상으로 넣어주면 된다. 여기선 테스트를 위해 @Import를 사용한다.
 */
@Slf4j
//@Import(AspectV1.class)
//@Import(AspectV2.class)
//@Import(AspectV3.class)
//@Import({AspectV4.LogAspect.class, AspectV4.TxAspect.class})
@Import(AspectV5.class)
@SpringBootTest
public class AopTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    void aopInfo() {
        log.info("isAopProxy, orderService={}", AopUtils.isAopProxy(orderService));
        log.info("isAopProxy, orderRepository={}", AopUtils.isAopProxy(orderRepository));
    }

    @Test
    void success() {
        orderService.orderItem("itemA");
    }

    @Test
    void exception() {
        assertThatThrownBy(() -> orderService.orderItem("ex"))
                .isInstanceOf(IllegalStateException.class);
    }
}
