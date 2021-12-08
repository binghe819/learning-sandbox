package com.binghe.springbootredisjedisvslettuce;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("post")
public class Post {

    @Id
    private String id;
    private int age;
    private LocalDateTime refreshTime;

    @Builder
    public Post(String id, int age, LocalDateTime refreshTime) {
        this.id = id;
        this.age = age;
        this.refreshTime = refreshTime;
    }
}
