package com.binghe.springbatchlearningtest.chunk_item;

import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class UserItemWriter implements ItemWriter<String> {

    @Override
    public void write(List<? extends String> items) throws Exception {
        items.forEach(System.out::println);
    }
}
