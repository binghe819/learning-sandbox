package com.binghe.springbootjpaexample2.shoppin_mall.api;

import com.binghe.springbootjpaexample2.shoppin_mall.domain.Order;
import com.binghe.springbootjpaexample2.shoppin_mall.repository.OrderRepository;
import com.binghe.springbootjpaexample2.shoppin_mall.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * ----- ManyToOne, OneToOne 관계 성능 최적화 -----
 * Order
 * Order -> Member
 * Order -> Delivery
 * ------------------------
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    /**
     * 무한 루프에 빠지는 예시
     * Order를 가져와서 Member를 조회 -> Member에서 List<Order>를 조회 -> Order마다 Member 조회 (무한 루프)
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findByOrderSearchBadPractice(new OrderSearch());

        // 지연로딩된 것 강제 초기화 (정말.. 답 없는 코드)
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
        }
        return all;
    }
}
