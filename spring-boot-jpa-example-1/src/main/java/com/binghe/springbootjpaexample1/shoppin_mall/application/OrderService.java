package com.binghe.springbootjpaexample1.shoppin_mall.application;

import com.binghe.springbootjpaexample1.shoppin_mall.domain.*;
import com.binghe.springbootjpaexample1.shoppin_mall.repository.ItemRepository;
import com.binghe.springbootjpaexample1.shoppin_mall.repository.MemberRepository;
import com.binghe.springbootjpaexample1.shoppin_mall.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 회원
        Member member = memberRepository.findById(memberId);
        if (Objects.isNull(member)) {
            throw new IllegalStateException("존재하지 않는 회원입니다.");
        }

        // 상품
        Item item = itemRepository.findById(itemId);
        if (Objects.isNull(item)) {
            throw new IllegalStateException("존재하지 않는 상품입니다.");
        }

        // 배송정보
        Delivery delivery = new Delivery(null, null, member.getAddress(), DeliveryStatus.READY);

        // 주문 상품 생성
        OrderItem orderItem = OrderItem.of(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.of(member, delivery, orderItem);

        orderRepository.save(order);
        return order.getId();
    }


    /**
     * 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 조회
        Order order = orderRepository.findbyId(orderId);
        // 주문 취소
        order.cancel();
    }

    /**
     * 검색
     */


}
