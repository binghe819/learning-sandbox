package com.binghe.springredisserial;

import com.binghe.springredisserial.post.Comment;
import com.binghe.springredisserial.post.Post;
import com.binghe.springredisserial.post.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class PostRedisTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @DisplayName("opsForValue (Interface ValueOperation) - Strings를 쉽게 직렬/역직렬화 해준다.")
    @Test
    void opsForValue() {
        // given
        String key = "string key";
        String value = "string value";
        redisTemplate.delete(key);

        // when
        ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();

        stringStringValueOperations.set(key, value); // set
        String result = stringStringValueOperations.get(key); //get

        // then
        assertThat(result).isEqualTo(value);
    }

    @DisplayName("JSON 직렬화/역직렬화 테스트")
    @Test
    void serializing_and_deserializing_json() {
        // given
        User user = User.createMock();
        List<Comment> comments = Comment.createMocks();
        Post post = Post.createMock("글 제목~~", "글 내용~~", comments, user);
        String key = "post:" + post.getId();
        redisTemplate.delete(key);

        // when
        ValueOperations<String, Post> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, post);

        Post result = valueOperations.get("post:" + post.getId());

        // then
        assertThat(result.getId()).isInstanceOf(Long.class);
        assertThat(result.getId()).isEqualTo(2147484647L);
        assertThat(result.getCreatedAt()).isInstanceOf(LocalDateTime.class);
    }

    @DisplayName("JSON 역직렬화 테스트")
    @Test
    void deserializing_json() {
        // given
        String key = "post:2147484647";

        // when
        ValueOperations<String, Post> valueOperations = redisTemplate.opsForValue();
        Post result = valueOperations.get(key);

        // then
        assertThat(result.getId()).isInstanceOf(Long.class);
    }

}