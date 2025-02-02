package com.binghe.domain;

import lombok.Getter;

@Getter
public class Count {

    private int findByIdCount;
    private int findByIdsCount;

    public Count() {
        findByIdCount = 0;
        findByIdsCount = 0;
    }

    public void countFindIdCount() {
        this.findByIdCount++;
    }

    public void countFindIdsCount() {
        this.findByIdsCount++;
    }

    public void reset() {
        this.findByIdsCount = 0;
        this.findByIdCount = 0;
    }
}
