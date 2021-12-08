package com.binghe.embeddedredis.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("point")
public class Point implements Serializable {

    @Id
    private String id;
    private Long amount;
    private LocalDateTime refreshTime;

    public Point(String id, Long amount, LocalDateTime refreshTime) {
        this.id = id;
        this.amount = amount;
        this.refreshTime = refreshTime;
    }

    public void refresh(long amount, LocalDateTime refreshTime) {
        if (refreshTime.isAfter(this.refreshTime)) {
            this.amount = amount;
            this.refreshTime = refreshTime;
        }
    }

    public String getId() {
        return id;
    }

    public Long getAmount() {
        return amount;
    }

    public LocalDateTime getRefreshTime() {
        return refreshTime;
    }
}
