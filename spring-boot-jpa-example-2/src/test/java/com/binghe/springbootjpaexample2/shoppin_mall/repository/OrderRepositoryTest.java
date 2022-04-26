package com.binghe.springbootjpaexample2.shoppin_mall.repository;

import com.binghe.springbootjpaexample2.shoppin_mall.application.OrderService;
import com.binghe.springbootjpaexample2.shoppin_mall.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional
@SpringBootTest
class OrderRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void 테스트() {
        // given
        Member member = createMember();
        Item book = createBook();

        int orderCount = 5;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        OrderSearch orderSearch = new OrderSearch();
        orderSearch.setMemberName("binghe");
        orderSearch.setOrderStatus(OrderStatus.ORDER);

        // when
        List<Order> byOrderSearch = orderRepository.findByOrderSearch(orderSearch);

        for (Order search : byOrderSearch) {
            System.out.println(search.getMember().getName());
        }
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