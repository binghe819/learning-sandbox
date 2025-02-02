package com.binghe.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class Member {

    private final Long id;
    private final String name;
    private final String address;
}
