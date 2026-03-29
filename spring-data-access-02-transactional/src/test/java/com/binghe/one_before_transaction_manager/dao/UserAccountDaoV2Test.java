package com.binghe.one_before_transaction_manager.dao;

import com.binghe.domain.User;
import com.binghe.domain.UserAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountDaoV2Test {

    private static ConnectionFactory connectionFactory;
    private Connection conn;
    private UserDaoV2 userDaoV2;
    private UserAccountDaoV2 userAccountDaoV2;

    @BeforeAll
    static void setUpAll() {
        connectionFactory = new ConnectionFactory();
    }

    @BeforeEach
    void setUp() throws SQLException {
        conn = connectionFactory.getConnectionFromConnectionPool();
        conn.setAutoCommit(false);
        userDaoV2 = new UserDaoV2();
        userAccountDaoV2 = new UserAccountDaoV2();
    }

    @AfterEach
    void tearDown() throws SQLException {
        conn.rollback();
        conn.close();
    }

    @Test
    void insert_후_findByUserId로_조회() throws SQLException {
        Long userId = userDaoV2.insert(conn, new User(null, "binghe", "pw"));
        userAccountDaoV2.insert(conn, new UserAccount(userId, 10000L));

        UserAccount account = userAccountDaoV2.findByUserId(conn, userId);

        assertNotNull(account);
        assertEquals(userId, account.getUserId());
        assertEquals(10000L, account.getBalance());
    }

    @Test
    void findAll_여러건_조회() throws SQLException {
        Long userId1 = userDaoV2.insert(conn, new User(null, "user1", "pw1"));
        Long userId2 = userDaoV2.insert(conn, new User(null, "user2", "pw2"));
        userAccountDaoV2.insert(conn, new UserAccount(userId1, 1000L));
        userAccountDaoV2.insert(conn, new UserAccount(userId2, 2000L));

        List<UserAccount> accounts = userAccountDaoV2.findAll(conn);

        assertTrue(accounts.size() >= 2);
    }

    @Test
    void update_후_잔액_변경_확인() throws SQLException {
        Long userId = userDaoV2.insert(conn, new User(null, "binghe", "pw"));
        userAccountDaoV2.insert(conn, new UserAccount(userId, 5000L));

        userAccountDaoV2.update(conn, new UserAccount(userId, 9000L));

        UserAccount updated = userAccountDaoV2.findByUserId(conn, userId);
        assertEquals(9000L, updated.getBalance());
    }

    @Test
    void delete_후_조회시_null() throws SQLException {
        Long userId = userDaoV2.insert(conn, new User(null, "binghe", "pw"));
        userAccountDaoV2.insert(conn, new UserAccount(userId, 5000L));

        userAccountDaoV2.delete(conn, userId);

        assertNull(userAccountDaoV2.findByUserId(conn, userId));
    }

    @Test
    void findByUserId_존재하지않는_userId는_null() throws SQLException {
        UserAccount account = userAccountDaoV2.findByUserId(conn, Long.MAX_VALUE);

        assertNull(account);
    }
}
