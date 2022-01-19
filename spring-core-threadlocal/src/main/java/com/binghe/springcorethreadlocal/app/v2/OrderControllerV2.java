package com.binghe.springcorethreadlocal.app.v2;

import com.binghe.springcorethreadlocal.trace.TraceStatus;
import com.binghe.springcorethreadlocal.trace.hellotrace.HelloTraceV1;
import com.binghe.springcorethreadlocal.trace.hellotrace.HelloTraceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV2 {

    private final OrderServiceV2 orderService;
    private final HelloTraceV2 trace;

    @GetMapping("/v2/request")
    public String request(String itemId) {

        TraceStatus status = null;
        try {
            status = trace.begin("OrderController.request()");
            orderService.orderItem(status.getTraceId(), itemId);
            trace.end(status);
            return "ok";
        } catch (IllegalStateException e) {
            trace.exception(status, e);
            throw e;
        }
    }
}
