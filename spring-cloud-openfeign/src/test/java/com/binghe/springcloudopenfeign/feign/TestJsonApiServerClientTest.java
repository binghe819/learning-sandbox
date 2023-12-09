package com.binghe.springcloudopenfeign.feign;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
class TestJsonApiServerClientTest {

    @Autowired
    private TestJsonApiServerClient testJsonApiServerClient;

    @Test
    void public_api_get_entry_test() {
        // given, when, then
        assertThatCode(() -> {
            List<TestJsonApiServerPostDto> posts = testJsonApiServerClient.getPosts();
            posts.forEach(System.out::println);
        }).doesNotThrowAnyException();
    }
}
