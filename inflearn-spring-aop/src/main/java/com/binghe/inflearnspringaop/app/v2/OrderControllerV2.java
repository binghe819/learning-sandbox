package com.binghe.inflearnspringaop.app.v2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
// 수동 등록하기위해 Controller를 사용하지 않음.
@RequestMapping
@ResponseBody
public class OrderControllerV2 {

    private final OrderServiceV2 orderServiceV2;

    public OrderControllerV2(OrderServiceV2 orderServiceV2) {
        this.orderServiceV2 = orderServiceV2;
    }

    @RequestMapping("/v2/request")
    public String request(String itemId) {
        orderServiceV2.orderItem(itemId);
        return "ok";
    }

    @RequestMapping("/v2/no-log")
    public String nolog() {
        return "ok";
    }
}
