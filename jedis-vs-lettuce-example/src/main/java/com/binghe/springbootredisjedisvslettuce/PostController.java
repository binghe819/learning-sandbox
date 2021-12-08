package com.binghe.springbootredisjedisvslettuce;

import java.time.LocalDateTime;
import java.util.SplittableRandom;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostRepository postRepository;
    private final AtomicLong pk = new AtomicLong(0L);

    @GetMapping("/save")
    public String save() {
        String pkId = String.valueOf(pk.incrementAndGet());
        LocalDateTime now = LocalDateTime.now();

        Post post = Post.builder()
            .id(pkId)
            .age(26)
            .refreshTime(now)
            .build();

        log.info("###### saved post id : {} ######", pkId);

        postRepository.save(post);

        return "success";
    }

    @GetMapping("/posts/{id}")
    public String get(@PathVariable String id) {
        return postRepository.findById(id)
            .map(Post::getId)
            .orElseThrow(RuntimeException::new);
    }

    // 1 ~ 1,000,000 사이 랜덤값 생성
    private String createId() {
        SplittableRandom random = new SplittableRandom();
        return String.valueOf(random.nextInt(1, 1_000_000));
    }
}
