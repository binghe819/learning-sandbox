package com.example.jpaqueryoptimization.application.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PostWithUserDto {

    // 게시물 기본 정보
    private Long id;
    private String title;
    private String content;

    // User Profile 정보
    private Long userId;
    private String name;
    private String address;
    private String phoneNumber;
}
