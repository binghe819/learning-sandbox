package com.binghe.one_before_transaction_manager.service;

import com.binghe.domain.User;
import com.binghe.domain.UserAccount;
import com.binghe.one_before_transaction_manager.dao.ConnectionFactory;
import com.binghe.one_before_transaction_manager.dao.UserAccountDaoV2;
import com.binghe.one_before_transaction_manager.dao.UserDaoV2;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountServiceV2Test {

    private static ConnectionFactory connectionFactory;
    private UserDaoV2 userDaoV2;
    private UserAccountDaoV2 userAccountDaoV2;
    private UserAccountServiceV2 userAccountService;

    private Long fromUserId;
    private Long toUserId;

    @BeforeAll
    static void setUpAll() {
        connectionFactory = new ConnectionFactory();
    }

    /**
     * UserAccountService는 내부에서 직접 커넥션을 획득하고 트랜잭션을 관리한다.
     * 따라서 테스트에서 트랜잭션 롤백으로 격리할 수 없으므로,
     * @BeforeEach에서 데이터를 직접 삽입하고 @AfterEach에서 삭제해 격리한다.
     */
    @BeforeEach
    void setUp() throws SQLException {
        userDaoV2 = new UserDaoV2();
        userAccountDaoV2 = new UserAccountDaoV2();
        userAccountService = new UserAccountServiceV2(connectionFactory, userAccountDaoV2);

        try (Connection conn = connectionFactory.getConnectionFromConnectionPool()) {
            conn.setAutoCommit(false);
            try {
                fromUserId = userDaoV2.insert(conn, new User(null, "from_user", "pw"));
                toUserId = userDaoV2.insert(conn, new User(null, "to_user", "pw"));
                userAccountDaoV2.insert(conn, new UserAccount(fromUserId, 10000L));
                userAccountDaoV2.insert(conn, new UserAccount(toUserId, 5000L));
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        try (Connection conn = connectionFactory.getConnectionFromConnectionPool()) {
            conn.setAutoCommit(false);
            try {
                userAccountDaoV2.delete(conn, fromUserId);
                userAccountDaoV2.delete(conn, toUserId);
                userDaoV2.delete(conn, fromUserId);
                userDaoV2.delete(conn, toUserId);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    @Test
    void transfer_성공시_잔액_정상_이동() throws SQLException {
        userAccountService.transfer(fromUserId, toUserId, 3000L);

        try (Connection conn = connectionFactory.getConnectionFromConnectionPool()) {
            UserAccount from = userAccountDaoV2.findByUserId(conn, fromUserId);
            UserAccount to = userAccountDaoV2.findByUserId(conn, toUserId);

            assertEquals(7000L, from.getBalance());
            assertEquals(8000L, to.getBalance());
        }
    }

    @Test
    void transfer_잔액부족시_예외_발생_및_롤백() throws SQLException {
        assertThrows(IllegalStateException.class, () ->
            userAccountService.transfer(fromUserId, toUserId, 99999L)
        );

        // 서비스 내부에서 rollback이 수행되므로 잔액 변화가 없어야 한다
        try (Connection conn = connectionFactory.getConnectionFromConnectionPool()) {
            UserAccount from = userAccountDaoV2.findByUserId(conn, fromUserId);
            UserAccount to = userAccountDaoV2.findByUserId(conn, toUserId);

            assertEquals(10000L, from.getBalance());
            assertEquals(5000L, to.getBalance());
        }
    }

    @Test
    void transfer_전액_이체() throws SQLException {
        userAccountService.transfer(fromUserId, toUserId, 10000L);

        try (Connection conn = connectionFactory.getConnectionFromConnectionPool()) {
            UserAccount from = userAccountDaoV2.findByUserId(conn, fromUserId);
            UserAccount to = userAccountDaoV2.findByUserId(conn, toUserId);

            assertEquals(0L, from.getBalance());
            assertEquals(15000L, to.getBalance());
        }
    }
}
