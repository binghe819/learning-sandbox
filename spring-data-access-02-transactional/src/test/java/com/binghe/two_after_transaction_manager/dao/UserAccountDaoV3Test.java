package com.binghe.two_after_transaction_manager.dao;

import com.binghe.domain.User;
import com.binghe.domain.UserAccount;
import com.binghe.two_after_transaction_manager.DataSourceFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountDaoV3Test {

    private static DataSource dataSource;
    private static DataSourceTransactionManager transactionManager;

    private UserDaoV3 userDaoV3;
    private UserAccountDaoV3 userAccountDaoV3;
    private TransactionStatus txStatus;

    @BeforeAll
    static void setUpAll() {
        dataSource = new DataSourceFactory().getDataSource();
        transactionManager = new DataSourceTransactionManager(dataSource);
    }

    @BeforeEach
    void setUp() {
        userDaoV3 = new UserDaoV3(dataSource);
        userAccountDaoV3 = new UserAccountDaoV3(dataSource);
        txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
    }

    @AfterEach
    void tearDown() {
        transactionManager.rollback(txStatus);
    }

    @Test
    void insert_후_findByUserId로_조회() throws SQLException {
        Long userId = userDaoV3.insert(new User(null, "binghe", "pw"));
        userAccountDaoV3.insert(new UserAccount(userId, 10000L));

        UserAccount account = userAccountDaoV3.findByUserId(userId);

        assertNotNull(account);
        assertEquals(userId, account.getUserId());
        assertEquals(10000L, account.getBalance());
    }

    @Test
    void update_후_잔액_변경_확인() throws SQLException {
        Long userId = userDaoV3.insert(new User(null, "binghe", "pw"));
        userAccountDaoV3.insert(new UserAccount(userId, 5000L));

        userAccountDaoV3.update(new UserAccount(userId, 9000L));

        UserAccount updated = userAccountDaoV3.findByUserId(userId);
        assertEquals(9000L, updated.getBalance());
    }

    @Test
    void delete_후_조회시_null() throws SQLException {
        Long userId = userDaoV3.insert(new User(null, "binghe", "pw"));
        userAccountDaoV3.insert(new UserAccount(userId, 5000L));

        userAccountDaoV3.delete(userId);

        assertNull(userAccountDaoV3.findByUserId(userId));
    }

    @Test
    void findByUserId_존재하지_않으면_null() throws SQLException {
        assertNull(userAccountDaoV3.findByUserId(Long.MAX_VALUE));
    }
}
