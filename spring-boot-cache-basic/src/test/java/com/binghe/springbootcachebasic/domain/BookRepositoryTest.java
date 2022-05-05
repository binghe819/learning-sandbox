package com.binghe.springbootcachebasic.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void independency() {
        assertThat(bookRepository).isNotNull();
        assertThat(entityManager).isNotNull();
    }

    @Test
    void saveAndGet() {
        // given
        Book savedBook = bookRepository.save(new Book("cache book", "isbn-123", false));

        entityManager.flush();
        entityManager.clear();

        // when
        Book findBook = bookRepository.findById(savedBook.getId())
                .orElse(null);

        // then
        assertThat(findBook).isNotNull();
        assertThat(findBook.getName()).isEqualTo(savedBook.getName());
        assertThat(findBook.getIsbn()).isEqualTo(savedBook.getIsbn());
    }

    @Test
    void findByIsbn() {
        // given
        String isbn = "test isbn";
        Book savedBook = bookRepository.save(new Book("cache book", isbn, false));

        entityManager.flush();
        entityManager.clear();

        // when
        Book findBook = bookRepository.findByIsbn(savedBook.getIsbn())
                .orElse(null);

        // then
        assertThat(findBook).isNotNull();
        assertThat(findBook.getName()).isEqualTo(savedBook.getName());
        assertThat(findBook.getIsbn()).isEqualTo(savedBook.getIsbn());
    }
}