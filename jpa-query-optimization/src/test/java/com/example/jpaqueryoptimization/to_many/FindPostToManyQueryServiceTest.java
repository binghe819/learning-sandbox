package com.example.jpaqueryoptimization.to_many;

import com.example.jpaqueryoptimization.application.PostToManyQueryService;
import com.example.jpaqueryoptimization.application.dto.CommentDto;
import com.example.jpaqueryoptimization.application.dto.PostDto;
import com.example.jpaqueryoptimization.config.QueryCounter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class FindPostToManyQueryServiceTest {

    @Autowired
    private PostToManyQueryService postToManyQueryService;
    @Autowired
    private QueryCounter queryCounter;

    @Test
    void 모든_Post_조회하며_Post마다_Comment를_같이_조회한다() {
        queryCounter.startCount();

        // when
        List<PostDto> posts = postToManyQueryService.findAll();
        List<CommentDto> comments = posts
                .stream()
                .map(PostDto::getCommentDtos)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        // then
        assertThat(posts).hasSize(3);
        System.out.println(comments.size());
        printQueryCount();
    }

    @Test
    void 모든_Post_조회하며_Post마다_Comment를_같이_조회한다_Post의_User와_Profile도_같이_조회한다() {
        queryCounter.startCount();

        // when
        List<PostDto> posts = postToManyQueryService.findAllWithUser();
        List<CommentDto> comments = posts
                .stream()
                .map(PostDto::getCommentDtos)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        // then
        assertThat(posts).hasSize(3);
        System.out.println(comments.size());
        printQueryCount();
    }

    private void printQueryCount() {
        System.out.println("##### 총 쿼리 개수 : " + queryCounter.getCount().getValue());
        queryCounter.endCount();
    }
}
