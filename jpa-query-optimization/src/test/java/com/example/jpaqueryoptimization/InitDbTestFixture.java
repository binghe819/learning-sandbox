package com.example.jpaqueryoptimization;

import com.example.jpaqueryoptimization.domain.*;
import com.example.jpaqueryoptimization.domain.repository.PostRepository;
import com.example.jpaqueryoptimization.domain.repository.TagRepository;
import com.example.jpaqueryoptimization.domain.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@org.springframework.context.annotation.Profile("test")
@Component
public class InitDbTestFixture {

    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PostRepository postRepository;

    public InitDbTestFixture(UserRepository userRepository, TagRepository tagRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    @PostConstruct
    public void init() {
        initTestFixture();
    }

    @Transactional
    public void initTestFixture() {
        User user1 = createUser("유저-빙허", "서울시 성동구 옥수동", "010-1234-1234");
        User user2 = createUser("유저-마크", "서울시 송파구 방이동", "010-5678-5678");

        Tag tag1 = createTag("테스트 태그 1");
        Tag tag2 = createTag("테스트 태그 2");
        Tag tag3 = createTag("테스트 태그 3");

        Post post1 = new Post("테스트 게시물 제목 1", "테스트 게시물 내용 1", user1);
        post1.addTags(Arrays.asList(tag1, tag2));
        post1.addComment(createComment("테스트 댓글 1", user1));
        post1.addComment(createComment("테스트 댓글 2", user2));
        postRepository.save(post1);

        Post post2 = new Post("테스트 게시물 제목 2", "테스트 게시물 내용 2", user2);
        post2.addTags(Arrays.asList(tag1, tag3));
        post2.addComment(createComment("테스트 댓글 3", user1));
        post2.addComment(createComment("테스트 댓글 4", user2));
        postRepository.save(post2);

        Post post3 = new Post("테스트 게시물 제목 3", "테스트 게시물 내용 3", user2);
        post3.addTags(Arrays.asList(tag2, tag3));
        post3.addComment(createComment("테스트 댓글 5", user2));
        post3.addComment(createComment("테스트 댓글 6", user2));
        postRepository.save(post3);
    }

    private User createUser(String name, String address, String phoneNumber) {
        User user = User.builder().name(name).build();
        Profile profile = Profile.builder()
                        .address(address)
                        .phoneNumber(phoneNumber)
                        .user(user)
                        .build();
        user.addProfile(profile);
        return userRepository.save(user);
    }

    private Comment createComment(String content, User writer) {
        return Comment.builder()
                .content(content)
                .writer(writer)
                .build();
    }

    private Tag createTag(String name) {
        Tag tag = new Tag(name);
        return tagRepository.save(tag);
    }
}
