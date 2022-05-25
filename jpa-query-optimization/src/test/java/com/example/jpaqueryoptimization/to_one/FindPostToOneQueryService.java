package com.example.jpaqueryoptimization.to_one;

import com.example.jpaqueryoptimization.application.PostToOneQueryService;
import com.example.jpaqueryoptimization.application.dto.PostWithUserDto;
import com.example.jpaqueryoptimization.config.QueryCounter;
import com.example.jpaqueryoptimization.domain.*;
import com.example.jpaqueryoptimization.domain.repository.TagRepository;
import com.example.jpaqueryoptimization.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
public class FindPostToOneQueryService {

    @Autowired
    private PostToOneQueryService postToOneQueryService;
    @Autowired
    private QueryCounter queryCounter;

    @Test
    void 모든_Post_조회하며_Post마다_작성자의_프로필을_같이_조회한다() {
        queryCounter.startCount();

        // when
        List<PostWithUserDto> result = postToOneQueryService.findAllWithUserProfile();

        // then
        assertThat(result).hasSize(3);
        assertThat(queryCounter.getCount().getValue()).isEqualTo(5);
        result.forEach(it -> System.out.println(it.toString()));

        printQueryCount();
    }

    @Test
    void 모든_Post_조회하며_Post마다_작성자의_프로필을_같이_조회한다_User_fetch_join() {
        queryCounter.startCount();

        // when
        List<PostWithUserDto> result = postToOneQueryService.findAllWithUserProfileWithFetchJoin();

        // then
        assertThat(result).hasSize(3);
        assertThat(queryCounter.getCount().getValue()).isEqualTo(3);
        result.forEach(it -> System.out.println(it.toString()));

        printQueryCount();
    }

    @Test
    void 모든_Post_조회하며_Post마다_작성자의_프로필을_같이_조회한다_User_Profile_모두_fetch_join() {
        queryCounter.startCount();

        // when
        List<PostWithUserDto> result = postToOneQueryService.findAllWithUserProfileWithFetchJoin_optimization();

        // then
        assertThat(result).hasSize(3);
        assertThat(queryCounter.getCount().getValue()).isEqualTo(1);
        result.forEach(it -> System.out.println(it.toString()));

        printQueryCount();
    }

    @Test
    void 모든_Post_조회하며_Post마다_작성자의_프로필을_같이_조회한다_User_Profile_모두_fetch_join_페이징() {
        queryCounter.startCount();

        // when
        List<PostWithUserDto> result1 = postToOneQueryService.findAllPostsPageable(PageRequest.of(0, 2));
        List<PostWithUserDto> result2 = postToOneQueryService.findAllPostsPageable(PageRequest.of(1, 2));


        // then
        assertThat(result1).hasSize(2);
        assertThat(result2).hasSize(1);
        assertThat(queryCounter.getCount().getValue()).isEqualTo(1 + 1);
        result1.forEach(it -> System.out.println(it.toString()));
        System.out.println("---------- 2 페이지 ----------");
        result2.forEach(it -> System.out.println(it.toString()));

        printQueryCount();
    }

    @Test
    void 모든_Post_조회하며_Post마다_작성자의_프로필을_같이_조회한다_User_Profile_모두_fetch_join_DTO_바로_조회() {
        queryCounter.startCount();

        // when
        List<PostWithUserDto> result = postToOneQueryService.findAllPostWithUserDto();

        // then
        assertThat(result).hasSize(3);
        assertThat(queryCounter.getCount().getValue()).isEqualTo(1);
        result.forEach(it -> System.out.println(it.toString()));

        printQueryCount();
    }

    private void printQueryCount() {
        System.out.println("##### 총 쿼리 개수 : " + queryCounter.getCount().getValue());
        queryCounter.endCount();
    }
}
