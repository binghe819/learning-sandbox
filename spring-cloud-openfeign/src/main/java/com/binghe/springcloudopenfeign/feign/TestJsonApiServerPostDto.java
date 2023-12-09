package com.binghe.springcloudopenfeign.feign;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TestJsonApiServerPostDto {

    private Long id;

    private String title;

    private String content;

    private String category;

    private String writer;
}
