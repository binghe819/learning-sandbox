package com.binghe.springbootdbreplicationjpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.binghe.springbootdbreplicationjpa.domain.User;
import com.binghe.springbootdbreplicationjpa.domain.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("실제 DB에 테스트해보기 위한 테스트 코드")
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class UserRepositoryDevTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User(null, "test", 26);
    }

    @DisplayName("유저 저장 및 조회")
    @Test
    void save() {
        // given
        User savedUser = userRepository.save(user);

        // when
        User findUser = userRepository.findById(savedUser.getId())
            .orElse(null);

        // then
        assertThat(findUser).isNotNull();
        assertThat(findUser)
            .usingRecursiveComparison()
            .isEqualTo(savedUser);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteById(user.getId());
    }
}
