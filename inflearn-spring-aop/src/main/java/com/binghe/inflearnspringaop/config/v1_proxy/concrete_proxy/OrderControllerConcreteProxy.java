package com.binghe.inflearnspringaop.config.v1_proxy.concrete_proxy;

import com.binghe.inflearnspringaop.app.v2.OrderControllerV2;
import com.binghe.inflearnspringaop.app.v2.OrderServiceV2;
import com.binghe.inflearnspringaop.trace.TraceStatus;
import com.binghe.inflearnspringaop.trace.logtrace.LogTrace;

public class OrderControllerConcreteProxy extends OrderControllerV2 {

    private final OrderControllerV2 target;
    private final LogTrace logTrace;

    public OrderControllerConcreteProxy(OrderControllerV2 target, LogTrace logTrace) {
        // 어차피 target이 service의 의존성을 가지고있기때문에 null로 처리.
        super(null);
        this.target = target;
        this.logTrace = logTrace;
    }

    @Override
    public String request(String itemId) {
        TraceStatus status = null;
        try {
            status = logTrace.begin("OrderController.save()");
            // target 호출
            target.request(itemId);

            logTrace.end(status);
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }
        return "ok";
    }

    @Override
    public String nolog() {
        return target.nolog();
    }
}
