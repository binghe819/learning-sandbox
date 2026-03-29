package com.binghe.three_transactional.service;

import com.binghe.domain.User;
import com.binghe.domain.UserAccount;
import com.binghe.three_transactional.AppConfig;
import com.binghe.three_transactional.dao.UserAccountDaoV4;
import com.binghe.three_transactional.dao.UserDaoV4;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Transactional 서비스의 통합 테스트.
 *
 * 서비스 테스트에 클래스 레벨 @Transactional을 사용하지 않는 이유:
 * - UserAccountServiceV4의 @Transactional은 PROPAGATION_REQUIRED(기본값)이다.
 * - 테스트 메서드에도 @Transactional이 있으면 서비스의 트랜잭션이 테스트 트랜잭션에 합류(join)한다.
 * - 서비스가 commit()을 호출해도 outer 트랜잭션(테스트)이 아직 끝나지 않았으므로 실제 DB 반영이 되지 않는다.
 * - 이 경우 "서비스가 커밋한 결과를 검증"하는 테스트 의도 자체가 성립하지 않는다.
 *
 * 따라서 서비스 테스트는 트랜잭션 없이 실행하고,
 * @BeforeEach / @AfterEach에서 PlatformTransactionManager를 주입받아 직접 데이터를 셋업/정리한다.
 * 서비스의 @Transactional이 독립적으로 커밋/롤백되므로 실제 운영과 동일한 흐름으로 검증할 수 있다.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
class UserAccountServiceV4Test {

    @Autowired
    private UserAccountServiceV4 userAccountServiceV4;

    @Autowired
    private UserDaoV4 userDaoV4;

    @Autowired
    private UserAccountDaoV4 userAccountDaoV4;

    @Autowired
    private PlatformTransactionManager transactionManager;

    private Long fromUserId;
    private Long toUserId;

    @BeforeEach
    void setUp() throws SQLException {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            fromUserId = userDaoV4.insert(new User(null, "from_user", "pw"));
            toUserId = userDaoV4.insert(new User(null, "to_user", "pw"));
            userAccountDaoV4.insert(new UserAccount(fromUserId, 10000L));
            userAccountDaoV4.insert(new UserAccount(toUserId, 5000L));
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
            userAccountDaoV4.delete(fromUserId);
            userAccountDaoV4.delete(toUserId);
            userDaoV4.delete(fromUserId);
            userDaoV4.delete(toUserId);
            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            throw e;
        }
    }

    @Test
    void transfer_성공시_잔액_정상_이동() throws SQLException {
        userAccountServiceV4.accountTransfer(fromUserId, toUserId, 3000L);

        // 서비스 @Transactional이 커밋한 결과를 별도 트랜잭션으로 읽어 검증한다.
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            UserAccount from = userAccountDaoV4.findByUserId(fromUserId);
            UserAccount to = userAccountDaoV4.findByUserId(toUserId);
            assertEquals(7000L, from.getBalance());
            assertEquals(8000L, to.getBalance());
        } finally {
            transactionManager.rollback(status);
        }
    }

    @Test
    void transfer_잔액부족시_예외_발생_및_롤백() throws SQLException {
        // 서비스 내부에서 rollback이 수행된다.
        assertThrows(IllegalStateException.class, () ->
            userAccountServiceV4.accountTransfer(fromUserId, toUserId, 99999L)
        );

        // 롤백됐으므로 잔액 변화가 없어야 한다.
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            UserAccount from = userAccountDaoV4.findByUserId(fromUserId);
            UserAccount to = userAccountDaoV4.findByUserId(toUserId);
            assertEquals(10000L, from.getBalance());
            assertEquals(5000L, to.getBalance());
        } finally {
            transactionManager.rollback(status);
        }
    }

    @Test
    void transfer_전액_이체() throws SQLException {
        userAccountServiceV4.accountTransfer(fromUserId, toUserId, 10000L);

        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            UserAccount from = userAccountDaoV4.findByUserId(fromUserId);
            UserAccount to = userAccountDaoV4.findByUserId(toUserId);
            assertEquals(0L, from.getBalance());
            assertEquals(15000L, to.getBalance());
        } finally {
            transactionManager.rollback(status);
        }
    }
}
