package com.example.jpaqueryoptimization.application;

import com.example.jpaqueryoptimization.application.dto.CommentDto;
import com.example.jpaqueryoptimization.application.dto.PostDto;
import com.example.jpaqueryoptimization.domain.Comment;
import com.example.jpaqueryoptimization.domain.Post;
import com.example.jpaqueryoptimization.domain.Profile;
import com.example.jpaqueryoptimization.domain.User;
import com.example.jpaqueryoptimization.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class PostToManyQueryService {

    private final PostRepository postRepository;

    public List<PostDto> findAll() {
        List<Post> posts = postRepository.findAll();
        return posts
                .stream()
                .map(this::convertToPostDtos)
                .collect(toList());
    }

    public List<PostDto> findAllWithUser() {
        List<Post> posts = postRepository.findAllWithUserAndProfile();
        return posts
                .stream()
                .map(this::convertToPostDtos)
                .collect(toList());
    }

    private PostDto convertToPostDtos(Post post) {
        User writer = post.getWriter();
        Profile profile = writer.getProfile();
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .commentDtos(convertToCommendDtos(post.getComments()))
                .userId(writer.getId())
                .name(writer.getName())
                .address(profile.getAddress())
                .phoneNumber(profile.getPhoneNumber())
                .build();
    }

    private List<CommentDto> convertToCommendDtos(List<Comment> comments) {
        return comments
                .stream()
                .map(comment -> {
                    User writer = comment.getWriter();
                    Profile profile = writer.getProfile();
                    return CommentDto.builder()
                            .id(comment.getId())
                            .content(comment.getContent())
                            .userId(writer.getId())
                            .name(writer.getName())
                            .address(profile.getAddress())
                            .phoneNumber(profile.getPhoneNumber())
                            .build();
                })
                .collect(toList());
    }
}
