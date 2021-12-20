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
import org.springframework.data.domain.PageRequest;

@DisplayName("페이징 + 컬렉션 패치 조인 테스트")
@DataJpaTest
public class UserRepositoryPagingTestWithoutBatchSize {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    TestEntityManager entityManager;

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
        flushAndClear();
    }

    @DisplayName("페이징 + 컬렉션 Fetch Join - 앞 5명 유저의 정보 및 게시물 조회 -> WARN 에러 발생")
    @Test
    void findAllWithPost() {
        List<User> users = userRepository.findAllWithPost(PageRequest.of(0, 5));

        List<Integer> postSumPerUser = calculatePostSumPerUser(users);
//        List<Integer> commentSumPerUser = calculateCommentSumPerUser(users);

        // 페이지 사이즈에 맞게 데이터를 가져오지만, 쿼리에 LIMIT으로 필터링하지 않고, 전체 데이터를 가져와서 JPA가 필터링한다.
        assertThat(users)
            .hasSize(5)
            .extracting("name")
            .containsExactly("빙허0", "빙허1", "빙허2", "빙허3", "빙허4");
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
