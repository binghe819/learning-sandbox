package com.binghe.springbootcachebasic.presentation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BookDto {

    private String name;

    private String isbn;

    private boolean isUsed;
}
