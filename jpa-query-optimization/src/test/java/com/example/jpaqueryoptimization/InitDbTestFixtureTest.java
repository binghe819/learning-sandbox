package com.example.jpaqueryoptimization;

import com.example.jpaqueryoptimization.config.QueryCounter;
import com.example.jpaqueryoptimization.domain.Comment;
import com.example.jpaqueryoptimization.domain.Post;
import com.example.jpaqueryoptimization.domain.PostTag;
import com.example.jpaqueryoptimization.domain.User;
import com.example.jpaqueryoptimization.domain.repository.PostRepository;
import com.example.jpaqueryoptimization.domain.repository.UserRepository;
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
public class InitDbTestFixtureTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private QueryCounter queryCounter;

    @Test
    void 유저_픽스처_테스트() {
        queryCounter.startCount();

        // when
        List<User> users = userRepository.findAll();

        // then
        assertThat(users).hasSize(2);
        users.stream().forEach(System.out::println);

        printQueryCount();
    }

    @Test
    void 게시물_픽스처_테스트() {
        queryCounter.startCount();

        // when
        List<Post> posts = postRepository.findAll();

        // then
        assertThat(posts).hasSize(3);

        printQueryCount();
    }

    @Test
    void 게시물_태그_픽스처_테스트() {
        queryCounter.startCount();

        // given
        List<Post> posts = postRepository.findAll();

        // when
        List<PostTag> postTags = posts
                .stream()
                .map(Post::getPostTags)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        // then
        assertThat(postTags).hasSize(6);
        postTags
                .forEach(postTag -> {
                    System.out.println(postTag.getPost().getTitle() + "의 태그 : " + postTag.getTag().getName());
                });

        printQueryCount();
    }

    @Test
    void 게시물_댓글_픽스처_테스트() {
        queryCounter.startCount();

        // given
        List<Post> posts = postRepository.findAll();

        // when
        List<Comment> comments = posts
                .stream()
                .map(Post::getComments)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        // then
        assertThat(comments).hasSize(6);
        comments
                .forEach(comment -> {
                    System.out.println(comment.getPost().getTitle() + "의 댓글 : " + comment.getContent());
                });

        printQueryCount();
    }

    private void printQueryCount() {
        System.out.println("##### 총 쿼리 개수 : " + queryCounter.getCount().getValue());
        queryCounter.endCount();
    }
}
