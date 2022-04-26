package com.binghe.springbootjpaexample2.shoppin_mall.application;

import com.binghe.springbootjpaexample2.shoppin_mall.domain.*;
import com.binghe.springbootjpaexample2.shoppin_mall.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void 상품주문_성공() {
        // given
        Member member = createMember();

        Item book = createBook();

        int orderCount = 5;

        // when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
        Order savedOrder = orderRepository.findbyId(orderId);

        // then
        assertThat(savedOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(savedOrder.getOrderItems().size()).isEqualTo(1);
        assertThat(savedOrder.getTotalPrice()).isEqualTo(10_000 * orderCount);
        assertThat(book.getStockQuantity()).isEqualTo(5);
    }

    @Test
    void 상품주문_실패_재고수량초과() {
        // given
        Member member = createMember();

        Item book = createBook();

        int orderCount = 100;

        // when, then
        assertThatCode(() -> orderService.order(member.getId(), book.getId(), orderCount))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 상품주문취소_성공() {
        // given
        Member member = createMember();
        Item book = createBook();

        int orderCount = 5;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);
        Order canceledOrder = orderRepository.findbyId(orderId);

        // then
        assertThat(canceledOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity()).isEqualTo(10);
    }

    private Member createMember() {
        Member member = new Member("binghe", new Address("서울", "몰라", "몰라"));
        entityManager.persist(member);
        return member;
    }

    private Item createBook() {
        Item book = new Book("작가", "isbn");
        book.setPrice(10_000);
        book.setName("테스트용 책");
        book.setStockQuantity(10);
        entityManager.persist(book);
        return book;
    }
}