package com.binghe.two_after_transaction_manager.dao;

import com.binghe.domain.User;
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

class UserDaoV3Test {

    private static DataSource dataSource;
    private static DataSourceTransactionManager transactionManager;

    private UserDaoV3 userDaoV3;
    private TransactionStatus txStatus;

    @BeforeAll
    static void setUpAll() {
        dataSource = new DataSourceFactory().getDataSource();
        /**
         * DataSourceTransactionManager는 DataSource 기반의 PlatformTransactionManager 구현체다.
         * getTransaction() 호출 시 DataSource에서 커넥션을 가져와
         * TransactionSynchronizationManager에 스레드 로컬로 바인딩한다.
         */
        transactionManager = new DataSourceTransactionManager(dataSource);
    }

    /**
     * 각 테스트 전에 트랜잭션을 시작한다.
     * DataSourceUtils.getConnection()을 사용하는 UserDaoV3는
     * 이 트랜잭션에 바인딩된 커넥션을 자동으로 공유하게 된다.
     */
    @BeforeEach
    void setUp() {
        userDaoV3 = new UserDaoV3(dataSource);
        txStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
    }

    /**
     * 각 테스트 후 트랜잭션을 롤백해 DB에 데이터가 남지 않도록 한다.
     */
    @AfterEach
    void tearDown() {
        transactionManager.rollback(txStatus);
    }

    @Test
    void insert_후_findById로_조회() throws SQLException {
        Long id = userDaoV3.insert(new User(null, "binghe", "pass123"));

        User found = userDaoV3.findById(id);

        assertNotNull(found);
        assertEquals("binghe", found.getName());
        assertEquals("pass123", found.getPassword());
    }

    @Test
    void update_후_변경사항_확인() throws SQLException {
        Long id = userDaoV3.insert(new User(null, "before", "oldpw"));

        userDaoV3.update(new User(id, "after", "newpw"));

        User updated = userDaoV3.findById(id);
        assertEquals("after", updated.getName());
        assertEquals("newpw", updated.getPassword());
    }

    @Test
    void delete_후_조회시_null() throws SQLException {
        Long id = userDaoV3.insert(new User(null, "toDelete", "pw"));

        userDaoV3.delete(id);

        assertNull(userDaoV3.findById(id));
    }

    @Test
    void findById_존재하지_않으면_null() throws SQLException {
        assertNull(userDaoV3.findById(Long.MAX_VALUE));
    }
}
