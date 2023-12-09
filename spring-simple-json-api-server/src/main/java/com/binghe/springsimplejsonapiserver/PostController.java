package com.binghe.springsimplejsonapiserver;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostRepository postRepository;

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getPosts() {
        return ResponseEntity.ok(postRepository.findAll()
                .stream()
                .map(it -> PostDto.builder().id(it.getId()).title(it.getTitle()).content(it.getContent()).category(it.getCategory()).writer(it.getWriter()).build())
                .toList()
        );
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable("id") Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
        return ResponseEntity.ok(PostDto.builder().id(post.getId()).title(post.getTitle()).content(post.getContent()).category(post.getCategory()).writer(post.getWriter()).build());
    }

    @PostMapping("/post")
    public ResponseEntity<String> savePost(@RequestBody PostDto postDto) {
        Post post = Post.builder().title(postDto.getTitle()).content(postDto.getContent()).category(postDto.getCategory()).writer(postDto.getWriter()).build();
        postRepository.save(post);
        return ResponseEntity.ok("success");
    }
}
