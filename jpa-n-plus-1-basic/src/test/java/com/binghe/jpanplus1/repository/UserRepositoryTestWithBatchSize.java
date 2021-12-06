package com.binghe.jpanplus1.repository;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

@DisplayName("둘 이상의 컬렉션 Fetch Join 테스트 - Batch Size 설정 O")
@TestPropertySource(properties = "spring.jpa.properties.hibernate.default_batch_fetch_size=1000") // 옵션 적용
@DataJpaTest
public class UserRepositoryTestWithBatchSize {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        for (int i = 0; i < 2; i++) {
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
        flushAndClear();
    }

    @DisplayName("Lazy 로딩 + batch_size - 각 유저가 작성한 모드 글과 모든 댓글 조회 (쿼리 3번)")
    @Test
    void findAll() {
        // given
        List<User> users = userRepository.findAll();

        // when
        List<Integer> postSumPerUser = calculatePostSumPerUser(users);

        List<Integer> commentSumPerUser = calculateCommentSumPerUser(users);

        // then
        assertThat(users).hasSize(2);
        assertThat(postSumPerUser)
            .hasSize(2)
            .allMatch(userPostSum -> userPostSum == 1);
        assertThat(commentSumPerUser)
            .hasSize(2)
            .allMatch(userCommentSum -> userCommentSum == 2);
    }

    @DisplayName("Post는 Fetch Join + Comment는 Lazy (batch_size) - 각 유저가 작성한 모드 글과 모든 댓글 조회 (쿼리 2번)")
    @Test
    void findAllFetchJoinPost() {
        // given
        List<User> users = userRepository.findAllFetchJoinPost();

        // when
        List<Integer> postSumPerUser = calculatePostSumPerUser(users);

        List<Integer> commentSumPerUser = calculateCommentSumPerUser(users);

        // then
        assertThat(users).hasSize(2);
        assertThat(postSumPerUser)
            .hasSize(2)
            .allMatch(userPostSum -> userPostSum == 1);
        assertThat(commentSumPerUser)
            .hasSize(2)
            .allMatch(userCommentSum -> userCommentSum == 2);
    }

    private List<Integer> calculatePostSumPerUser(List<User> users) {
        return users.stream()
            .map(User::getPosts)
            .map(posts -> posts.stream().map(Post::getContent).collect(toList()))
            .map(List::size)
            .collect(toList());
    }

    private List<Integer> calculateCommentSumPerUser(List<User> users) {
        return users.stream()
            .map(User::getComments)
            .map(comments -> comments.stream().map(Comment::getContent).collect(toList()))
            .map(List::size)
            .collect(toList());
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
