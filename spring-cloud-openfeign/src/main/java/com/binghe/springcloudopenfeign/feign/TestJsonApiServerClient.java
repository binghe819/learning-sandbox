package com.binghe.springcloudopenfeign.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * 테스트 서버 호출 Client
 */
@FeignClient(name = "testJsonApiServerClient", url = "${external.jsonplaceholder.host}")
public interface TestJsonApiServerClient {

    @GetMapping("/posts")
    List<TestJsonApiServerPostDto> getPosts();

    @GetMapping("/post/{id}")
    TestJsonApiServerPostDto getPost(@PathVariable("id") Long id);

    @PostMapping("/post")
    String savePost(TestJsonApiServerPostDto testJsonApiServerPostDto);
}
