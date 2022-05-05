package com.binghe.springbootcachebasic.presentation;

import com.binghe.springbootcachebasic.application.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class BookController {

    private final BookService bookService;

    @PostMapping("/books")
    public Long createBook(@RequestParam(value = "name") String name,
                           @RequestParam(value = "isbn") String isbn) {
        return bookService.createBook(name, isbn);
    }

    @GetMapping("/books/{id}")
    public BookDto findById(@PathVariable(value = "id") Long id) {
        return bookService.findById(id);
    }

    @GetMapping("/books/isbn/{isbn}")
    public BookDto findByIsbn(@PathVariable(value = "isbn") String isbn) {
        return bookService.findByIsbn(isbn);
    }
}
