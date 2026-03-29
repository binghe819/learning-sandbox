package com.binghe.one_before_transaction_manager.dao;

import com.binghe.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoV2Test {

    private static ConnectionFactory connectionFactory;
    private Connection conn;
    private UserDaoV2 userDaoV2;

    @BeforeAll
    static void setUpAll() {
        connectionFactory = new ConnectionFactory();
    }

    /**
     * 각 테스트마다 커넥션 풀에서 커넥션을 가져오고 autoCommit을 false로 설정한다.
     * 테스트가 끝나면 @AfterEach에서 rollback하므로 DB에 데이터가 남지 않는다.
     */
    @BeforeEach
    void setUp() throws SQLException {
        conn = connectionFactory.getConnectionFromConnectionPool();
        conn.setAutoCommit(false);
        userDaoV2 = new UserDaoV2();
    }

    @AfterEach
    void tearDown() throws SQLException {
        conn.rollback();
        conn.close();
    }

    @Test
    void insert_후_findById로_조회() throws SQLException {
        User user = new User(null, "binghe", "pass123");

        Long id = userDaoV2.insert(conn, user);

        User found = userDaoV2.findById(conn, id);
        assertNotNull(found);
        assertEquals("binghe", found.getName());
        assertEquals("pass123", found.getPassword());
    }

    @Test
    void findAll_여러건_조회() throws SQLException {
        userDaoV2.insert(conn, new User(null, "user1", "pw1"));
        userDaoV2.insert(conn, new User(null, "user2", "pw2"));

        List<User> users = userDaoV2.findAll(conn);

        assertTrue(users.size() >= 2);
    }

    @Test
    void update_후_변경사항_확인() throws SQLException {
        Long id = userDaoV2.insert(conn, new User(null, "before", "oldpw"));

        userDaoV2.update(conn, new User(id, "after", "newpw"));

        User updated = userDaoV2.findById(conn, id);
        assertEquals("after", updated.getName());
        assertEquals("newpw", updated.getPassword());
    }

    @Test
    void delete_후_조회시_null() throws SQLException {
        Long id = userDaoV2.insert(conn, new User(null, "toDelete", "pw"));

        userDaoV2.delete(conn, id);

        assertNull(userDaoV2.findById(conn, id));
    }

    @Test
    void findById_존재하지않는_id는_null() throws SQLException {
        User found = userDaoV2.findById(conn, Long.MAX_VALUE);

        assertNull(found);
    }
}
