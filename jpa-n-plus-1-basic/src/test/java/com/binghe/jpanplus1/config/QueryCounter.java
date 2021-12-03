package com.binghe.jpanplus1.config;

public class QueryCounter {

    private Count count;
    private boolean countable;

    public QueryCounter() {
        this.count = new Count(0L);
        countable = false;
    }

    public void startCount() {
        this.countable = true;
        this.count = new Count(0L);
    }

    public Count getCount() {
        return count;
    }

    public void countOne() {
        count = count.countOne();
    }

    public void endCount() {
        this.countable = false;
    }

    public boolean isCountable() {
        return countable;
    }
}
