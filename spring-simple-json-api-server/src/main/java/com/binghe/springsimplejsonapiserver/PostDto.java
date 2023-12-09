package com.binghe.springsimplejsonapiserver;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class PostDto {

    private Long id;

    private String title;

    private String content;

    private String category;

    private String writer;
}
