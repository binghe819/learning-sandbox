package com.binghe.two_after_transaction_manager.service;

import com.binghe.domain.UserAccount;
import com.binghe.two_after_transaction_manager.dao.UserAccountDaoV3;
import com.binghe.two_after_transaction_manager.dao.UserDaoV3;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.SQLException;

public class UserAccountServiceV3 {

    private final PlatformTransactionManager transactionManager;
    private final UserDaoV3 userDaoV3;
    private final UserAccountDaoV3 userAccountDaoV3;

    public UserAccountServiceV3(PlatformTransactionManager transactionManager, UserDaoV3 userDaoV3, UserAccountDaoV3 userAccountDaoV3) {
        this.transactionManager = transactionManager;
        this.userDaoV3 = userDaoV3;
        this.userAccountDaoV3 = userAccountDaoV3;
    }

    /**
     * PlatformTransactionManager를 사용한 계좌 이체.
     *
     * 동작 흐름:
     * 1. transactionManager.getTransaction()
     *    - 내부적으로 DataSource에서 커넥션을 하나 가져와 TransactionSynchronizationManager에 스레드 로컬로 바인딩한다.
     *    - autoCommit을 false로 설정한다.
     *    - TransactionStatus를 반환하며, 이 객체로 이후에 commit/rollback을 제어한다.
     *
     * 2. DAO 메서드 호출
     *    - DAO 내부의 DataSourceUtils.getConnection()이 TransactionSynchronizationManager에서
     *      이미 바인딩된 커넥션을 꺼내 사용한다. → 같은 커넥션, 같은 트랜잭션 안에서 실행된다.
     *    - DataSourceUtils.releaseConnection()은 트랜잭션 중인 커넥션을 실제로 닫지 않고 유지한다.
     *
     * 3. transactionManager.commit() / rollback()
     *    - commit: DB에 변경사항을 반영하고 커넥션을 풀에 반납한다.
     *    - rollback: 변경사항을 모두 되돌리고 커넥션을 풀에 반납한다.
     *    - 두 경우 모두 TransactionSynchronizationManager에서 커넥션 바인딩을 해제한다.
     *
     * V2(one_before_transaction_manager)과의 차이:
     * - V2: 서비스가 커넥션을 직접 생성하고 DAO에 파라미터로 넘겼다. → 서비스-DAO 간 강한 결합.
     * - V3: 서비스는 TransactionManager에만 의존하고, DAO는 DataSourceUtils로 스레드 로컬 커넥션을 조회한다.
     *       → 서비스와 DAO가 커넥션을 직접 주고받지 않아도 같은 트랜잭션에 참여할 수 있다.
     */
    public void accountTransfer(Long fromId, Long toId, long money) throws SQLException {
        // 트랜잭션 시작: DataSource에서 커넥션을 가져와 스레드 로컬에 바인딩하고 autoCommit=false
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            UserAccount fromAccount = userAccountDaoV3.findByUserId(fromId);
            if (fromAccount.getBalance() < money) {
                throw new IllegalStateException("잔액이 부족합니다. 현재 잔액: " + fromAccount.getBalance());
            }

            userAccountDaoV3.update(new UserAccount(fromId, fromAccount.getBalance() - money));

            UserAccount toAccount = userAccountDaoV3.findByUserId(toId);
            userAccountDaoV3.update(new UserAccount(toId, toAccount.getBalance() + money));

            // 커밋: DB 반영 후 커넥션을 풀에 반납하고 스레드 로컬 바인딩 해제
            transactionManager.commit(status);
        } catch (Exception e) {
            // 롤백: 변경사항 취소 후 커넥션을 풀에 반납하고 스레드 로컬 바인딩 해제
            transactionManager.rollback(status);
            throw e;
        }
    }
}
