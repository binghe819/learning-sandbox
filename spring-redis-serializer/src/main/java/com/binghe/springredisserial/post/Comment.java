package com.binghe.springredisserial.post;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    private Long id;

    private String content;

    private User writer;

    public static List<Comment> createMocks() {
        List<Comment> result = new ArrayList<>();
        result.add(Comment.builder()
                .id(1L)
                .content("comment1")
                .writer(User.createMock())
                .build());
        result.add(Comment.builder()
                .id(2L)
                .content("comment2")
                .writer(User.createMock())
                .build());
        result.add(Comment.builder()
                .id(3L)
                .content("comment2")
                .writer(User.createMock())
                .build());
        result.add(Comment.builder()
                .id(4L)
                .content("comment2")
                .writer(User.createMock())
                .build());
        return result;
    }
}
