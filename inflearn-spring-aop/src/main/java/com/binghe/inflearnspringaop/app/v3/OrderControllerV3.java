package com.binghe.inflearnspringaop.app.v3;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderControllerV3 {

    private final OrderServiceV3 orderServiceV3;

    public OrderControllerV3(OrderServiceV3 orderServiceV3) {
        this.orderServiceV3 = orderServiceV3;
    }

    @RequestMapping("/v3/request")
    public String request(String itemId) {
        orderServiceV3.orderItem(itemId);
        return "ok";
    }

    @RequestMapping("/v3/no-log")
    public String nolog() {
        return "ok";
    }
}
