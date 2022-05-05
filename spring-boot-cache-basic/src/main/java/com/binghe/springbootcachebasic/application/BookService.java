package com.binghe.springbootcachebasic.application;

import com.binghe.springbootcachebasic.domain.Book;
import com.binghe.springbootcachebasic.domain.BookRepository;
import com.binghe.springbootcachebasic.presentation.BookDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookDto findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .stream()
                .map(book -> new BookDto(book.getName(), book.getIsbn(), book.isUsed()))
                .findFirst()
                .orElse(null);
    }

    public BookDto findById(Long id) {
        return bookRepository.findById(id)
                .stream()
                .map(book -> new BookDto(book.getName(), book.getIsbn(), book.isUsed()))
                .findFirst()
                .orElse(null);
    }

    @Transactional
    public Long createBook(String name, String isbn) {
        Book book = Book.builder()
                .name(name)
                .isbn(isbn)
                .isUsed(false)
                .build();
        Book savedBook = bookRepository.save(book);
        return savedBook.getId();
    }
}
