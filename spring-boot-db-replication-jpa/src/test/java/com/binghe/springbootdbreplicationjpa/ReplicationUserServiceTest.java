package com.binghe.springbootdbreplicationjpa;

import static org.assertj.core.api.Assertions.assertThat;

import com.binghe.springbootdbreplicationjpa.application.UserService;
import com.binghe.springbootdbreplicationjpa.domain.User;
import javax.sql.DataSource;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@DisplayName("UserService와 로깅을 이용한 Slave 조회 테스트")
@AutoConfigureTestDatabase(replace = Replace.NONE)
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class ReplicationUserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private DataSource routingDataSource;

    @Test
    void dependency() {
        assertThat(userService).isNotNull();
        assertThat(routingDataSource).isNotNull();
    }

    @DisplayName("Master 쓰기 및 Slave 조회 테스트 (로깅을 이용)")
    @Test
    void save() {
        // given
        User user = new User(null, "mark", 26);
        User savedUser = userService.save(user);

        // when
        User findUser = userService.findById(savedUser.getId());
        User findUser2 = userService.findById(savedUser.getId());
        User findUser3 = userService.findById(savedUser.getId());

        // then
        assertThat(findUser)
            .usingRecursiveComparison()
            .isEqualTo(savedUser);
        assertThat(findUser2)
            .usingRecursiveComparison()
            .isEqualTo(savedUser);
        assertThat(findUser3)
            .usingRecursiveComparison()
            .isEqualTo(savedUser);
    }
}
