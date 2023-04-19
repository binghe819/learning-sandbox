package com.binghe.springredisserial.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;

    private String name;

    public static User createMock() {
        return User.builder()
                .id(1L)
                .name("binghe")
                .build();
    }
}
