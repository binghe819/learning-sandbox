package com.binghe.jpanplus1.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.binghe.jpanplus1.config.QueryCounter;
import com.binghe.jpanplus1.domain.Comment;
import com.binghe.jpanplus1.domain.Post;
import com.binghe.jpanplus1.domain.PostRepository;
import com.binghe.jpanplus1.domain.User;
import com.binghe.jpanplus1.domain.UserRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("N + 1 테스트 - Post당 Comment 하나씩")
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class PostServiceSingleCommentTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QueryCounter queryCounter;

    @BeforeEach
    void setUp() {
        User user = userRepository.save(User.builder().name("빙허").build());

        for (int i = 0; i < 10; i++) {
            Post post = Post.builder()
                .title("N + 1 문제" + i)
                .content("N + 1 문제를 해결해보아요." + i)
                .writer(user)
                .build();

            post.addComment(Comment.builder().content("너무 좋네요 :)" + i).writer(user).build());
            postRepository.save(post);
        }
    }

    @DisplayName("문제 해결 전 - 모든 Post의 Comments를 조회하면 N + 1 문제가 발생한다.")
    @Test
    void findAllCommentContents() {
        queryCounter.startCount();

        List<String> allCommentContents = postService.findAllCommentContents();

        assertThat(allCommentContents).hasSize(10);

        assertThat(queryCounter.getCount().getValue()).isEqualTo(11);
        printQueryCount();
    }

    @DisplayName("Fetch Join - fetch join을 통해 N + 1 문제를 해결할 수 있다.")
    @Test
    void findAllCommentContentsByFetchJoin() {
        queryCounter.startCount();

        List<String> allCommentContents = postService.findAllCommentContentsByFetchJoin();

        assertThat(allCommentContents).hasSize(10);
        assertThat(queryCounter.getCount().getValue()).isEqualTo(1);
        printQueryCount();
    }

    @DisplayName("@EntityGraph - @EntityGraph를 통해 N + 1 문제를 해결할 수 있다.")
    @Test
    void findAllCommentContentsByEntityGraph() {
        queryCounter.startCount();

        List<String> allCommentContents = postService.findAllCommentContentsByEntityGraph();

        assertThat(allCommentContents).hasSize(10);
        assertThat(queryCounter.getCount().getValue()).isEqualTo(1);
        printQueryCount();
    }

    private void printQueryCount() {
        System.out.println("##### 총 쿼리 개수 : " + queryCounter.getCount().getValue());
        queryCounter.endCount();
    }
}
