package com.binghe.springjpaquerycounter.query_counter;

import lombok.Getter;

@Getter
public class QueryCounter {

    private Count count;
    private boolean countable;

    public QueryCounter() {
        countable = false;
        count = new Count(0L);
    }

    public void startCount() {
        countable = true;
        count = new Count(0L);
    }

    public void countOne() {
        if (!isCountable()) {
            throw new RuntimeException("[Error] 아직 카운트를 시작하지 않았습니다.");
        }
        count = count.countOne();
    }

    public void endCount() {
        countable = false;
    }
}
