package com.binghe.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Member {

    private final Long id;

    private final String name;

    private final String address;

    private final String description;
}
