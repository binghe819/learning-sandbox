package com.binghe.springjpaquerycounter.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void dependency() {
        assertThat(postRepository).isNotNull();
    }

    @DisplayName("Post 저장 테스트")
    @Test
    void save() {
        // given
        String title = "post save test";
        String content = "post content save test";

        // when
        Post savedPost = savePost(title, content);

        // then
        assertThat(savedPost.getId()).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo(title);
        assertThat(savedPost.getContent()).isEqualTo(content);
    }

    @DisplayName("Post에 Comment 추가 테스트")
    @Test
    void appendComment() {
        // given
        Post post = new Post("title", "content");
        String commentContent = "comment test";
        Comment comment = new Comment(commentContent);

        // when
        post.addComment(comment);
        Post savedPost = postRepository.save(post);
        flushAndClear();

        // then
        Post findPost = postRepository.findById(savedPost.getId())
            .orElse(null);
        assertThat(findPost).isNotNull();
        assertThat(findPost.getTitle()).isEqualTo("title");
        assertThat(findPost.getContent()).isEqualTo("content");
        assertThat(findPost.getComments()).hasSize(1);
    }

    @DisplayName("N + 1 발생 테스트 - Post들을 조회한다.")
    @Test
    void findPosts() {
        // given
        Post post1 = createPostWithComment();
        Post post2 = createPostWithComment();
        Post post3 = createPostWithComment();

        // when
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        flushAndClear();

        // then
        List<Post> findPosts = postRepository.findAll();
        assertThat(findPosts).hasSize(3);
        assertThat(findPosts.get(0).getComments()).hasSize(2);
        assertThat(findPosts.get(1).getComments()).hasSize(2);
        assertThat(findPosts.get(2).getComments()).hasSize(2);
    }

    private Post savePost(String title, String content) {
        return postRepository.save(new Post(title, content));
    }

    private Post createPostWithComment() {
        Comment comment1 = new Comment("comment1");
        Comment comment2 = new Comment("comment2");
        Post post = new Post("title1", "content1");

        post.addComment(comment1);
        post.addComment(comment2);
        return post;
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
