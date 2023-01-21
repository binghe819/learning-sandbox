package com.binghe.inflearnspringaop.config.v1_proxy.concrete_proxy;

import com.binghe.inflearnspringaop.app.v2.OrderRepositoryV2;
import com.binghe.inflearnspringaop.app.v2.OrderServiceV2;
import com.binghe.inflearnspringaop.trace.TraceStatus;
import com.binghe.inflearnspringaop.trace.logtrace.LogTrace;

public class OrderServiceConcreteProxy extends OrderServiceV2 {

    private final OrderServiceV2 target;
    private final LogTrace logTrace;

    public OrderServiceConcreteProxy(OrderServiceV2 target, LogTrace logTrace) {
        // 어차피 target이 repository에 대한 의존성을 가지고있기때문에 null처리.
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public void orderItem(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderService.orderItem()");
            // target 호출
            target.orderItem(itemId);

            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
    }
}
