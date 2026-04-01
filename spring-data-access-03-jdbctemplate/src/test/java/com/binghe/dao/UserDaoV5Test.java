package com.binghe.dao;

import com.binghe.AppConfig;
import com.binghe.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 실제 MySQL DB를 사용하는 통합 테스트.
 * @Transactional을 붙이면 각 테스트가 끝난 후 자동으로 롤백되어 DB 상태가 유지된다.
 */
@SpringJUnitConfig(AppConfig.class)
@Transactional
class UserDaoV5Test {

    @Autowired
    private UserDaoV5 userDaoV5;

    @Test
    void insert_findById() {
        User user = new User(null, "alice", "pw123");

        Long id = userDaoV5.insert(user);

        User found = userDaoV5.findById(id);
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("alice");
        assertThat(found.getPassword()).isEqualTo("pw123");
    }

    @Test
    void update() {
        Long id = userDaoV5.insert(new User(null, "bob", "pw456"));

        userDaoV5.update(new User(id, "bob-updated", "pw456-updated"));

        User found = userDaoV5.findById(id);
        assertThat(found.getName()).isEqualTo("bob-updated");
        assertThat(found.getPassword()).isEqualTo("pw456-updated");
    }

    @Test
    void delete() {
        Long id = userDaoV5.insert(new User(null, "charlie", "pw789"));

        userDaoV5.delete(id);

        assertThat(userDaoV5.findById(id)).isNull();
    }

    @Test
    void findById_notFound_returnsNull() {
        assertThat(userDaoV5.findById(-1L)).isNull();
    }
}
