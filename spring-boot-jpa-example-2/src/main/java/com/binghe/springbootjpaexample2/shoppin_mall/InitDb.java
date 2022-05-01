package com.binghe.springbootjpaexample2.shoppin_mall;

import com.binghe.springbootjpaexample2.shoppin_mall.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            Member member = createMember("binghe A", new Address("서울", "성동구", "12314"));
            em.persist(member);

            Book book1 = createBook("JPA1 BOOK", 10_000, 1_00);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOK", 20_000, 2_00);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.of(book1, 10_000, 1);
            OrderItem orderItem2 = OrderItem.of(book2, 20_000, 2);
            Order order = Order.of(member, createDelivery(member), orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInit2() {
            Member member = createMember("binghe B", new Address("서울", "서초구", "12324"));
            em.persist(member);

            Book book1 = createBook("SPRING1 BOOK", 20_000, 200);
            em.persist(book1);

            Book book2 = createBook("SPRING2 BOOK", 40_000, 300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.of(book1, 20_000, 3);
            OrderItem orderItem2 = OrderItem.of(book2, 40_000, 4);
            Order order = Order.of(member, createDelivery(member), orderItem1, orderItem2);
            em.persist(order);
        }

        private Member createMember(String name, Address address) {
            Member member = new Member(name, address);
            return member;
        }

        private Book createBook(String name, int price, int stockQuantity) {
            String author = "binghe";
            String isbn = "isbn~";
            Book book = new Book(author, isbn);
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }
}


