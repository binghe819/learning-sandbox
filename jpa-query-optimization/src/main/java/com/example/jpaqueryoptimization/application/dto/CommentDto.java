package com.example.jpaqueryoptimization.application.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CommentDto {
    // 댓글 정보
    private Long id;
    private String content;

    // 작성자 Profile 정보
    private Long userId;
    private String name;
    private String address;
    private String phoneNumber;
}
