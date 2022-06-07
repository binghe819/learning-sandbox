package com.binghe.springbatchlearningtest.chunk_item;

import org.springframework.batch.item.ItemProcessor;

public class UserItemProcessor implements ItemProcessor<User, String> {

    @Override
    public String process(User item) throws Exception {
        // User의 이름을 가져와 대문자로 변환후 반환.
        String userName = item.getName();
        return userName.toUpperCase();
    }
}
