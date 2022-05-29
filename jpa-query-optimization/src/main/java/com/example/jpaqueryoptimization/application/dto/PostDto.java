package com.example.jpaqueryoptimization.application.dto;

import com.example.jpaqueryoptimization.domain.PostTag;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PostDto {
    // 게시물 기본 정보
    private Long id;
    private String title;
    private String content;

    // 댓글 정보
    private List<CommentDto> commentDtos;
    private List<PostTagDto> postTagDtos;

    // 작성자 Profile 정보
    private Long userId;
    private String name;
    private String address;
    private String phoneNumber;
}
