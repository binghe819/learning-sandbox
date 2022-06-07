package com.binghe.springbatchlearningtest.chunk_item;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;

public class UserItemReader implements ItemReader<User> {

    private List<User> users;

    public UserItemReader(List<User> users) {
        this.users = new ArrayList<>(users);
    }

    @Override
    public User read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        // 만약 읽어갈 데이터가 있다면 하나씩 반환
        if (!users.isEmpty()) {
            return users.remove(0);
        }
        // 읽어갈 데이터가 없다면 null을 반환하여 더이상 read하지 못하도록 함.
        return null;
    }
}
