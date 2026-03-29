package com.binghe.three_transactional.service;

import com.binghe.domain.UserAccount;
import com.binghe.three_transactional.dao.UserAccountDaoV4;
import com.binghe.three_transactional.dao.UserDaoV4;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

/**
 * @Transactional을 사용한 계좌 이체 서비스.
 *
 * V3와의 핵심 차이:
 * - V3: PlatformTransactionManager를 직접 주입받아 getTransaction() / commit() / rollback()을
 *       비즈니스 로직 사이에 직접 호출했다. → 트랜잭션 부가 로직이 비즈니스 코드를 오염시켰다.
 *
 * - V4: @Transactional 하나로 선언만 하면 된다. 트랜잭션 시작/커밋/롤백은 Spring AOP 프록시가
 *       모두 처리하므로, 서비스 코드에는 순수한 비즈니스 로직만 남는다.
 *
 * Spring AOP 프록시 동작 흐름:
 * 1. 호출자가 UserAccountServiceV4.accountTransfer()를 호출한다.
 * 2. 실제로는 Spring이 생성한 프록시 객체의 메서드가 먼저 실행된다.
 * 3. 프록시는 TransactionInterceptor를 통해 트랜잭션을 시작한다.
 *    (DataSourceTransactionManager → DataSource에서 커넥션 획득 → TransactionSynchronizationManager에 바인딩)
 * 4. 실제 accountTransfer() 비즈니스 로직이 실행된다.
 *    (DAO들은 DataSourceUtils로 바인딩된 커넥션을 공유해 같은 트랜잭션에 참여한다.)
 * 5. 정상 종료 시 프록시가 commit() → 커넥션을 풀에 반납하고 바인딩 해제.
 *    RuntimeException 발생 시 프록시가 rollback() → 변경사항 취소 후 동일하게 정리.
 *
 * 주의: @Transactional이 동작하려면 반드시 Spring 컨테이너가 관리하는 빈이어야 한다.
 * 직접 new UserAccountServiceV4(...)로 생성하면 프록시가 적용되지 않아 트랜잭션이 동작하지 않는다.
 * → AppConfig 참고
 */
public class UserAccountServiceV4 {

    private final UserDaoV4 userDaoV4;
    private final UserAccountDaoV4 userAccountDaoV4;

    public UserAccountServiceV4(UserDaoV4 userDaoV4, UserAccountDaoV4 userAccountDaoV4) {
        this.userDaoV4 = userDaoV4;
        this.userAccountDaoV4 = userAccountDaoV4;
    }

    /**
     * PlatformTransactionManager 관련 코드가 완전히 사라지고 비즈니스 로직만 남는다.
     * 트랜잭션 시작/커밋/롤백은 AOP 프록시가 처리한다.
     *
     * rollbackFor: @Transactional은 기본적으로 RuntimeException과 Error에서만 롤백한다.
     * SQLException은 CheckedException이므로 명시적으로 rollbackFor에 지정해야 롤백된다.
     */
    @Transactional(rollbackFor = Exception.class)
    public void accountTransfer(Long fromId, Long toId, long money) throws SQLException {
        UserAccount fromAccount = userAccountDaoV4.findByUserId(fromId);
        if (fromAccount.getBalance() < money) {
            throw new IllegalStateException("잔액이 부족합니다. 현재 잔액: " + fromAccount.getBalance());
        }

        userAccountDaoV4.update(new UserAccount(fromId, fromAccount.getBalance() - money));

        UserAccount toAccount = userAccountDaoV4.findByUserId(toId);
        userAccountDaoV4.update(new UserAccount(toId, toAccount.getBalance() + money));
    }
}
