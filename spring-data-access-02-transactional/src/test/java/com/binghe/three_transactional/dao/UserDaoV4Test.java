package com.binghe.three_transactional.dao;

import com.binghe.domain.User;
import com.binghe.three_transactional.AppConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Spring TestContext Framework를 사용한 DAO 통합 테스트.
 *
 * @ExtendWith(SpringExtension.class): JUnit 5와 Spring TestContext Framework를 연결한다.
 *   Spring 컨텍스트를 테스트 생명주기에 맞게 로드/관리한다.
 *
 * @ContextConfiguration(classes = AppConfig.class): 테스트에서 사용할 Spring 컨텍스트를 지정한다.
 *   AppConfig에 등록된 DataSource, TransactionManager, DAO 빈들이 모두 로드된다.
 *
 * @Transactional (클래스 레벨): 각 테스트 메서드를 하나의 트랜잭션으로 실행한다.
 *   테스트가 끝나면 Spring Test가 자동으로 rollback해 DB에 데이터가 남지 않는다.
 *   (직접 rollback()을 호출하지 않아도 된다.)
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
@Transactional
class UserDaoV4Test {

    @Autowired
    private UserDaoV4 userDaoV4;

    @Test
    void insert_후_findById로_조회() throws SQLException {
        Long id = userDaoV4.insert(new User(null, "binghe", "pass123"));

        User found = userDaoV4.findById(id);

        assertNotNull(found);
        assertEquals("binghe", found.getName());
        assertEquals("pass123", found.getPassword());
    }

    @Test
    void update_후_변경사항_확인() throws SQLException {
        Long id = userDaoV4.insert(new User(null, "before", "oldpw"));

        userDaoV4.update(new User(id, "after", "newpw"));

        User updated = userDaoV4.findById(id);
        assertEquals("after", updated.getName());
        assertEquals("newpw", updated.getPassword());
    }

    @Test
    void delete_후_조회시_null() throws SQLException {
        Long id = userDaoV4.insert(new User(null, "toDelete", "pw"));

        userDaoV4.delete(id);

        assertNull(userDaoV4.findById(id));
    }

    @Test
    void findById_존재하지_않으면_null() throws SQLException {
        assertNull(userDaoV4.findById(Long.MAX_VALUE));
    }
}
