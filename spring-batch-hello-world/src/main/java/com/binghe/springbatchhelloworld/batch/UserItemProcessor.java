package com.binghe.springbatchhelloworld.batch;

import com.binghe.springbatchhelloworld.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class UserItemProcessor implements ItemProcessor<User, User> {

    @Override
    public User process(User user) throws Exception {
        final String firstName = user.getFirstName().toUpperCase();
        final String lastName = user.getLastName().toUpperCase();

        final User transformedPerson = new User(firstName, lastName);

        log.info("Converting (" + user + ") into (" + transformedPerson + ")");

        return transformedPerson;
    }
}
