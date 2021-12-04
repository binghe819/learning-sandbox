package com.binghe.jpanplus1.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.binghe.jpanplus1.config.QueryCounter;
import com.binghe.jpanplus1.domain.Comment;
import com.binghe.jpanplus1.domain.Post;
import com.binghe.jpanplus1.domain.PostRepository;
import com.binghe.jpanplus1.domain.User;
import com.binghe.jpanplus1.domain.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("N + 1 테스트 - Post당 Comment 여러개씩")
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class PostServiceMultiCommentTest {

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
        for (int i = 0; i < 10; i++) {
            User user = userRepository.save(User.builder().name("빙허" + i).build());

            Post post = Post.builder()
                .title("N + 1 문제" + i)
                .content("N + 1 문제를 해결해보아요." + i)
                .writer(user)
                .build();

            post.addComment(Comment.builder().content("너무 좋네요 :)" + i).writer(user).build());
            post.addComment(Comment.builder().content("매우 좋네요 :)" + i).writer(user).build());
            postRepository.save(post);
        }
    }

    @DisplayName("중복 제거 안한 테스트 - 데이터 뻥튀기로 인해 동일한 주소를 가리키는 객체 두 개를 중복해서 가지고 있는다. (카테시안 곱)")
    @Test
    void findAllCommentContentsByFetchJoin() {
        queryCounter.startCount();

        List<String> allCommentContents = postService.findAllCommentContentsByFetchJoin();

        assertThat(allCommentContents.size()).isNotEqualTo(20); // 20을 예상하지만 뻥튀기되어 40개가 나온다
        assertThat(allCommentContents).hasSize(40);
        assertThat(allCommentContents.get(0)).isSameAs(allCommentContents.get(2)); // 동일한 주소를 가리키는 객체
        assertThat(queryCounter.getCount().getValue()).isEqualTo(1);
        printQueryCount();
    }

    @DisplayName("중복 제거 - DISTINCT + fetch join")
    @Test
    void findAllCommentContentsByFetchJoinDistinct() {
        queryCounter.startCount();

        List<String> allCommentContents = postService.findAllCommentContentsByFetchJoinDistinct();

        assertThat(allCommentContents).hasSize(20);
        assertThat(queryCounter.getCount().getValue()).isEqualTo(1);
        printQueryCount();
    }

    @DisplayName("중복 제거 - DISTINCT + @EntityGraph")
    @Test
    void findAllCommentContentsByEntityGraphDistinct() {
        queryCounter.startCount();

        List<String> allCommentContents = postService.findAllCommentContentsByEntityGraphDistinct();

        assertThat(allCommentContents).hasSize(20);
        assertThat(queryCounter.getCount().getValue()).isEqualTo(1);
        printQueryCount();
    }

    @DisplayName("중복 제거 + 여러 단계 Fetch Join")
    @Test
    void findAllFetchJoinWithCommentWriter() {
        // given
        queryCounter.startCount();
        List<Post> posts = postRepository.findAllFetchJoinWithCommentWriter();

        // when
        // 기존의 Post의 Comments 가져오는 코드.
        List<String> comments = posts.stream()
            .flatMap(post -> post.getComments().stream().map(Comment::getContent))
            .collect(Collectors.toList());

        // (User는 Lazy이지만, 여러 단계의 Fetch Join을 통해 User도 한 번에 가져왔기 때문에 추가로 쿼리가 날아가지 않는다.)
        List<String> userNames = posts.stream()
            .map(post -> post.getWriter().getName())
            .collect(Collectors.toList());

        // then
        assertThat(comments).hasSize(20);
        assertThat(userNames).hasSize(10);
        assertThat(queryCounter.getCount().getValue()).isEqualTo(1);
        printQueryCount();
    }

    private void printQueryCount() {
        System.out.println("##### 총 쿼리 개수 : " + queryCounter.getCount().getValue());
        queryCounter.endCount();
    }
}
