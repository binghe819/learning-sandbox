package com.example.jpaqueryoptimization.application;

import com.example.jpaqueryoptimization.application.dto.PostWithUserDto;
import com.example.jpaqueryoptimization.domain.Post;
import com.example.jpaqueryoptimization.domain.Profile;
import com.example.jpaqueryoptimization.domain.User;
import com.example.jpaqueryoptimization.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostToOneQueryService {

    private final PostRepository postRepository;

    public List<PostWithUserDto> findAllWithUserProfile() {
        List<Post> posts = postRepository.findAll();
        return posts
                .stream()
                .map(this::toPostWithUserDto)
                .collect(toList());
    }

    public List<PostWithUserDto> findAllWithUserProfileWithFetchJoin() {
        List<Post> posts = postRepository.findAllWithUser();
        return posts
                .stream()
                .map(this::toPostWithUserDto)
                .collect(toList());
    }

    public List<PostWithUserDto> findAllWithUserProfileWithFetchJoin_optimization() {
        List<Post> posts = postRepository.findAllWithUserAndProfile();
        return posts
                .stream()
                .map(this::toPostWithUserDto)
                .collect(toList());
    }

    public List<PostWithUserDto> findAllPostsPageable(Pageable pageable) {
        List<Post> posts = postRepository.findAllPosts(pageable);
        return posts
                .stream()
                .map(this::toPostWithUserDto)
                .collect(toList());
    }

    public List<PostWithUserDto> findAllPostWithUserDto() {
        return postRepository.findPostWithUserDtos();
    }

    public PostWithUserDto toPostWithUserDto(Post post) {
        User writer = post.getWriter();
        Profile profile = writer.getProfile();
        return PostWithUserDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(writer.getId())
                .name(writer.getName())
                .address(profile.getAddress())
                .phoneNumber(profile.getPhoneNumber())
                .build();
    }
}
