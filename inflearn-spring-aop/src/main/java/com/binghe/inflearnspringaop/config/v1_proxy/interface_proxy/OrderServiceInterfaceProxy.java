package com.binghe.inflearnspringaop.config.v1_proxy.interface_proxy;

import com.binghe.inflearnspringaop.app.v1.OrderServiceV1;
import com.binghe.inflearnspringaop.trace.TraceStatus;
import com.binghe.inflearnspringaop.trace.logtrace.LogTrace;

public class OrderServiceInterfaceProxy implements OrderServiceV1 {

    private final OrderServiceV1 target;
    private final LogTrace logTrace;

    public OrderServiceInterfaceProxy(OrderServiceV1 target, LogTrace logTrace) {
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
