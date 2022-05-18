package com.binghe.springbootjpaexample2.shoppin_mall.api;

import com.binghe.springbootjpaexample2.shoppin_mall.domain.Address;
import com.binghe.springbootjpaexample2.shoppin_mall.domain.Order;
import com.binghe.springbootjpaexample2.shoppin_mall.domain.OrderStatus;
import com.binghe.springbootjpaexample2.shoppin_mall.repository.OrderRepository;
import com.binghe.springbootjpaexample2.shoppin_mall.repository.OrderSearch;
import com.binghe.springbootjpaexample2.shoppin_mall.repository.order.simplequery.OrderSimpleQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 첫번째 개선 - 엔티티를 DTO로 변환
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findByOrderSearchBadPractice(new OrderSearch());
        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 두번째 개선 - fetch join
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberAndDelivery();
        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 세번째 개선 - DTO로 바로 가져오기 (원하는 상태만 조회 -> 원하는 값만 SELECT)
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.name = order.getMember().getName();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();
            this.address = order.getDelivery().getAddress();
        }
    }
}
