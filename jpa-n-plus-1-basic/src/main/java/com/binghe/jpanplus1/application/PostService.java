package com.binghe.jpanplus1.application;

import com.binghe.jpanplus1.domain.Comment;
import com.binghe.jpanplus1.domain.Post;
import com.binghe.jpanplus1.domain.PostRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional(readOnly = true)
@Service
public class PostService {

    private PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<String> findAllCommentContents() {
        return extractCommentContents(postRepository.findAll());
    }

    public List<String> findAllCommentContentsByFetchJoin() {
        return extractCommentContents(postRepository.findAllFetchJoin());
    }

    public List<String> findAllCommentContentsByEntityGraph() {
        return extractCommentContents(postRepository.findAllEntityGraph());
    }

    public List<String> findAllCommentContentsByFetchJoinDistinct() {
        return extractCommentContents(postRepository.findAllFetchJoinDistinct());
    }

    public List<String> findAllCommentContentsByEntityGraphDistinct() {
        return extractCommentContents(postRepository.findAllEntityGraphDistinct());
    }


    private List<String> extractCommentContents(List<Post> posts) {
        return posts.stream()
            .flatMap(post -> post.getComments().stream().map(Comment::getContent))
            .collect(Collectors.toList());
    }
}
