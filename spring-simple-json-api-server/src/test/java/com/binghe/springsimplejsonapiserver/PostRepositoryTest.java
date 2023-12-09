package com.binghe.springsimplejsonapiserver;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void Post_Save_And_Find_By_Id() {
        // given
        Post post = Post.builder()
                .title("binghe post test title")
                .content("binghe post test content")
                .category("binghe post test category")
                .writer("binghe post test writer")
                .build();

        // when
        Post savePost = postRepository.save(post);
        entityManager.flush(); entityManager.clear();
        Post foundPost = postRepository.findById(savePost.getId()).orElse(null);

        // then
        assertThat(foundPost.getId()).isEqualTo(savePost.getId());
        assertThat(foundPost.getTitle()).isEqualTo(savePost.getTitle());
        assertThat(foundPost.getContent()).isEqualTo(savePost.getContent());
        assertThat(foundPost.getCategory()).isEqualTo(savePost.getCategory());
        assertThat(foundPost.getWriter()).isEqualTo(savePost.getWriter());
    }
}