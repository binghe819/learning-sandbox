package com.binghe.springbootjpaexample2.shoppin_mall.application;

import com.binghe.springbootjpaexample2.shoppin_mall.domain.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @DisplayName("준영속 상태를 업데이트하는 방법 - 영속 (더티 체킹)")
    @Test
    void updateTest_dirtyChecking() {
        // given
        Book book = new Book("작가", "isbn");
        book.setPrice(10_000);
        book.setName("테스트용 책");
        book.setStockQuantity(10);

        em.persist(book);
        em.flush();
        em.clear();

        // when (영속화된 엔티티를 찾아서 더티 체킹이용)
        Book savedBook = em.find(Book.class, book.getId()); // 이때 스냅샷이 저장.
        savedBook.setName("책 이름 수정");                     // 영속상태의 엔티티 내용 수정

        em.flush();                                         // flush하면서 스냅샷과 비교후 바뀐 값 UPDATE
        em.clear();

        // then
        Book updatedBook = em.find(Book.class, savedBook.getId());
        assertThat(updatedBook.getName()).isEqualTo("책 이름 수정"); // DB에 값이 바뀐 것을 볼 수 있다.
    }

    @DisplayName("준영속 상태를 업데이트하는 방법 - 병합 (merge)")
    public void updateTest_merge() {
        // given
        Book book = new Book("작가", "isbn");
        book.setPrice(10_000);
        book.setName("테스트용 책");
        book.setStockQuantity(10);

        em.persist(book);
        em.flush();
        em.clear();

        // when (식별자를 가진 준영속 엔티티를 병합하여 모든 내용을 DB에 엎어씌운다)
        Book detachedBook = new Book("작가", "isbn");
        book.setId(book.getId());
        book.setPrice(1_000); // 덮어씌워진다.
        book.setName(null);   // null이므로 DB에도 null로 저장된다. (가장 큰 문제! 상태 변경을 원하지 않지만, merge는 모든 상태를 업데이트한다.)
        book.setStockQuantity(10); // 덮어씌워진다.

        Book mergedBook = em.merge(detachedBook); // detachedBook이 아닌 mergedBook이 영속화된다.
        em.flush();
        em.clear();

        // then
        Book updatedBook = em.find(Book.class, mergedBook.getId());
        assertThat(updatedBook.getName()).isNull(); // null이기를 원하지않지만, 병합은 덮어씌우는 것이기때문에 null이 되어있다.
        assertThat(updatedBook.getPrice()).isEqualTo(1_000);
        assertThat(updatedBook.getStockQuantity()).isEqualTo(book.getStockQuantity());
    }
}
