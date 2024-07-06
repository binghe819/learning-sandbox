package com.binghe.javawebperformancetomcat.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Member {
    private String name;
    private int age;
    private String country;
    private String description;
}
