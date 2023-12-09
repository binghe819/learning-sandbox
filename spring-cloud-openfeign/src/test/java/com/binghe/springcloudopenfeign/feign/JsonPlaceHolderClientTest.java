package com.binghe.springcloudopenfeign.feign;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
class PublicApiClientTest {

    @Autowired

    @Test
    void public_api_get_entry_test() {
        // given, when, then
        assertThatCode(() -> {

        }).doesNotThrowAnyException();
    }
}