package com.example.jpaqueryoptimization.to_many;

import com.example.jpaqueryoptimization.application.PostToManyQueryService;
import com.example.jpaqueryoptimization.application.dto.CommentDto;
import com.example.jpaqueryoptimization.application.dto.PostDto;
import com.example.jpaqueryoptimization.config.QueryCounter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

//@TestPropertySource(properties = "spring.jpa.properties.hibernate.default_batch_fetch_size=1000")
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
        posts.forEach(post -> assertThat(post.getCommentDtos()).hasSize(2));
        assertThat(comments).hasSize(6);
        printQueryCount();
    }

    // ToOne은 Fetch Join으로 조회 쿼리 수 최적화
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
        posts.forEach(post -> assertThat(post.getCommentDtos()).hasSize(2));
        assertThat(comments).hasSize(6);
        printQueryCount();
    }

    // ToMany부분 Fetch Join으로 최적화와 한계 - Distinct 미적용
    @Test
    void 모든_Post_조회하며_Post마다_Comment를_같이_조회한다_Fetch_Join() {
        queryCounter.startCount();

        // when
        List<PostDto> posts = postToManyQueryService.findAllWithCommentFetchJoin();
        List<CommentDto> comments = posts
                .stream()
                .map(PostDto::getCommentDtos)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        // then
        assertThat(posts).hasSize(6); // 데이터 뻥튀기 발생 -> 기존엔 3개
        posts.forEach(post -> assertThat(post.getCommentDtos()).hasSize(2));
        assertThat(comments).hasSize(12); // 데이터 뻥튀기 발생 -> 기존엔 6개
        printQueryCount();
    }

    // ToMany부분 Fetch Join으로 최적화와 한계 - Distinct 적용
    @Test
    void 모든_Post_조회하며_Post마다_Comment를_같이_조회한다_Fetch_Join_Distinct() {
        queryCounter.startCount();

        // when
        List<PostDto> posts = postToManyQueryService.findAllWithCommentFetchJoinDistinct();
        List<CommentDto> comments = posts
                .stream()
                .map(PostDto::getCommentDtos)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        // then
        assertThat(posts).hasSize(3); // 데이터 뻥튀기 미발생
        posts.forEach(post -> assertThat(post.getCommentDtos()).hasSize(2));
        assertThat(comments).hasSize(6); // 데이터 뻥튀기 미발생
        printQueryCount();
    }

    // ToMany부분 Fetch Join으로 최적화와 한계 - 첫번째 페이징 불가능.
    // HHH000104: firstResult/maxResults specified with collection fetch; applying in memory! 발생
    @Test
    void 모든_Post_조회하며_Post마다_Comment를_같이_조회한다_Fetch_Join_Distinct_Paging() {
        queryCounter.startCount();

        // when
        List<PostDto> posts = postToManyQueryService.findAllWithCommentsFetchJoinDistinctPageable(PageRequest.of(1, 2));
        List<CommentDto> comments = posts
                .stream()
                .map(PostDto::getCommentDtos)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        // then
        assertThat(posts).hasSize(1); // 데이터는 제대로 가져오지만 모든 데이터를 가져와서 메모리에 올리고 페이징을 하게 된다.
        posts.forEach(post -> assertThat(post.getCommentDtos()).hasSize(2));
        assertThat(comments).hasSize(2);
        printQueryCount();
    }

    // ToMany부분 Fetch Join으로 최적화와 한계 - 두번째 한 개 이상의 ToMany Fetch Join 불가능.
    // MultipleBagFetchException 예외가 발생한다. -> JPA가 컴파일 타임에 확인해서 일단은 주석처리해둠.
//    @Test
//    void 모든_Post_조회하며_Post마다_Comment를_같이_조회한다_Fetch_Join_Distinct_Multi_ToMany() {
//        queryCounter.startCount();
//
//        // when
//        List<PostDto> posts = postToManyQueryService.findAllWithCommentsAndPostTagFetchJoin();
//        List<CommentDto> comments = posts
//                .stream()
//                .map(PostDto::getCommentDtos)
//                .flatMap(Collection::stream)
//                .collect(Collectors.toList());
//
//        printQueryCount();
//    }

    // ToMany부분 Batch Size로 최적화
    // 페이징과 두 개이상의 ToMany를 비교적 적은 쿼리로 조회할 수 있다.
    @Test
    void 모든_Post_조회하며_Post마다_Comment를_같이_조회한다_Batch_Size() {
        queryCounter.startCount();

        // when
        List<PostDto> posts = postToManyQueryService.findAllWithCommentsAndPostTagWithBatchSize(PageRequest.of(1, 2));
        List<CommentDto> comments = posts
                .stream()
                .map(PostDto::getCommentDtos)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        // then
        assertThat(posts).hasSize(1);
        posts.forEach(post -> assertThat(post.getCommentDtos()).hasSize(2));
        assertThat(comments).hasSize(2);
        printQueryCount();
    }

    private void printQueryCount() {
        System.out.println("##### 총 쿼리 개수 : " + queryCounter.getCount().getValue());
        queryCounter.endCount();
    }
}
