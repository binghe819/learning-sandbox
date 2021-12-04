package com.binghe.jpanplus1.repository;

import static org.assertj.core.api.Assertions.assertThat;

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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

@DisplayName("batch_size를 이용한 여러 단계 Fetch Join 테스트")
@TestPropertySource(properties = "spring.jpa.properties.hibernate.default_batch_fetch_size=1000") // 옵션 적용
@DataJpaTest
public class PostRepositoryMultiFetchJoinTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

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
        testEntityManager.flush();
        testEntityManager.clear();
    }

    @DisplayName("중복 제거 + 여러 단계 Fetch Join + batch_size")
    @Test
    void batch_size_findAllFetchJoinDistinct() {
        // given
        List<Post> posts = postRepository.findAllFetchJoinDistinct();

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
    }
}
