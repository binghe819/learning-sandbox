package com.binghe.two_after_transaction_manager.service;

import com.binghe.domain.User;
import com.binghe.domain.UserAccount;
import com.binghe.two_after_transaction_manager.DataSourceFactory;
import com.binghe.two_after_transaction_manager.dao.UserAccountDaoV3;
import com.binghe.two_after_transaction_manager.dao.UserDaoV3;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountServiceV3Test {

    private static DataSource dataSource;
    private static PlatformTransactionManager transactionManager;

    private UserDaoV3 userDaoV3;
    private UserAccountDaoV3 userAccountDaoV3;
    private UserAccountServiceV3 userAccountServiceV3;

    private Long fromUserId;
    private Long toUserId;

    @BeforeAll
    static void setUpAll() {
        dataSource = new DataSourceFactory().getDataSource();
        transactionManager = new DataSourceTransactionManager(dataSource);
    }

    /**
     * UserAccountServiceV3는 내부에서 transactionManager.getTransaction()을 직접 호출한다.
     * 서비스가 트랜잭션을 직접 commit/rollback하므로, 테스트에서 바깥에서 트랜잭션을 걸어 롤백하는
     * 방식은 쓸 수 없다. (서비스의 commit이 먼저 DB에 반영되기 때문)
     *
     * 따라서 @BeforeEach에서 테스트 데이터를 직접 commit해 삽입하고,
     * @AfterEach에서 직접 commit으로 삭제해 격리한다.
     */
    @BeforeEach
    void setUp() throws SQLException {
        userDaoV3 = new UserDaoV3(dataSource);
        userAccountDaoV3 = new UserAccountDaoV3(dataSource);
        userAccountServiceV3 = new UserAccountServiceV3(transactionManager, userDaoV3, userAccountDaoV3);

        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            fromUserId = userDaoV3.insert(new User(null, "from_user", "pw"));
            toUserId = userDaoV3.insert(new User(null, "to_user", "pw"));
            userAccountDaoV3.insert(new UserAccount(fromUserId, 10000L));
            userAccountDaoV3.insert(new UserAccount(toUserId, 5000L));
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @AfterEach
    void tearDown() throws SQLException {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            userAccountDaoV3.delete(fromUserId);
            userAccountDaoV3.delete(toUserId);
            userDaoV3.delete(fromUserId);
            userDaoV3.delete(toUserId);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Test
    void transfer_성공시_잔액_정상_이동() throws SQLException {
        userAccountServiceV3.accountTransfer(fromUserId, toUserId, 3000L);

        // 서비스 트랜잭션이 끝난 후 별도 트랜잭션으로 결과를 조회한다.
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            UserAccount from = userAccountDaoV3.findByUserId(fromUserId);
            UserAccount to = userAccountDaoV3.findByUserId(toUserId);
            assertEquals(7000L, from.getBalance());
            assertEquals(8000L, to.getBalance());
        } finally {
            transactionManager.rollback(status);
        }
    }

    @Test
    void transfer_잔액부족시_예외_발생_및_롤백() throws SQLException {
        assertThrows(IllegalStateException.class, () ->
            userAccountServiceV3.accountTransfer(fromUserId, toUserId, 99999L)
        );

        // 서비스 내부에서 rollback이 수행됐으므로 잔액 변화가 없어야 한다.
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            UserAccount from = userAccountDaoV3.findByUserId(fromUserId);
            UserAccount to = userAccountDaoV3.findByUserId(toUserId);
            assertEquals(10000L, from.getBalance());
            assertEquals(5000L, to.getBalance());
        } finally {
            transactionManager.rollback(status);
        }
    }

    @Test
    void transfer_전액_이체() throws SQLException {
        userAccountServiceV3.accountTransfer(fromUserId, toUserId, 10000L);

        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            UserAccount from = userAccountDaoV3.findByUserId(fromUserId);
            UserAccount to = userAccountDaoV3.findByUserId(toUserId);
            assertEquals(0L, from.getBalance());
            assertEquals(15000L, to.getBalance());
        } finally {
            transactionManager.rollback(status);
        }
    }
}
