package com.binghe.springjpaquerycounter.query_counter;

import lombok.Getter;

@Getter
public class Count {

    private long value;

    public Count(long value) {
        this.value = value;
    }

    public Count countOne() {
        return new Count(++value);
    }
}
