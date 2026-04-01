package com.binghe.service;

import com.binghe.AppConfig;
import com.binghe.dao.UserAccountDaoV5;
import com.binghe.dao.UserDaoV5;
import com.binghe.domain.User;
import com.binghe.domain.UserAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * UserAccountServiceV5 통합 테스트.
 *
 * 테스트에 @Transactional을 붙이지 않는 이유:
 *   - accountTransfer()의 @Transactional이 실제로 커밋/롤백하는 동작을 검증하려면
 *     서비스 메서드가 독립적인 트랜잭션을 시작·종료해야 한다.
 *   - 테스트에 @Transactional을 붙이면 서비스 메서드가 테스트 트랜잭션에 참여(REQUIRED)하여
 *     커밋이 일어나지 않으므로, 롤백 검증이 의미 없어진다.
 *
 * 대신 @BeforeEach / @AfterEach 에서 데이터를 직접 세팅/정리한다.
 */
@SpringJUnitConfig(AppConfig.class)
class UserAccountServiceV5Test {

    @Autowired
    private UserAccountServiceV5 userAccountServiceV5;

    @Autowired
    private UserDaoV5 userDaoV5;

    @Autowired
    private UserAccountDaoV5 userAccountDaoV5;

    private Long fromUserId;
    private Long toUserId;

    @BeforeEach
    void setUp() {
        fromUserId = userDaoV5.insert(new User(null, "from-user", "pw"));
        toUserId   = userDaoV5.insert(new User(null, "to-user",   "pw"));
        userAccountDaoV5.insert(new UserAccount(fromUserId, 10_000L));
        userAccountDaoV5.insert(new UserAccount(toUserId,    5_000L));
    }

    @AfterEach
    void tearDown() {
        userAccountDaoV5.delete(fromUserId);
        userAccountDaoV5.delete(toUserId);
        userDaoV5.delete(fromUserId);
        userDaoV5.delete(toUserId);
    }

    /**
     * 정상 이체: from 잔액 감소, to 잔액 증가, @Transactional 커밋 확인
     */
    @Test
    void accountTransfer_success() {
        userAccountServiceV5.accountTransfer(fromUserId, toUserId, 3_000L);

        assertThat(userAccountDaoV5.findByUserId(fromUserId).getBalance()).isEqualTo(7_000L);
        assertThat(userAccountDaoV5.findByUserId(toUserId).getBalance()).isEqualTo(8_000L);
    }

    /**
     * 잔액 부족: IllegalStateException 발생 + @Transactional 롤백으로 DB 상태 변경 없음
     */
    @Test
    void accountTransfer_insufficientBalance_throwsAndRollback() {
        assertThatThrownBy(() -> userAccountServiceV5.accountTransfer(fromUserId, toUserId, 99_999L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("잔액이 부족");

        // 롤백으로 인해 잔액이 원래 값 그대로여야 한다
        assertThat(userAccountDaoV5.findByUserId(fromUserId).getBalance()).isEqualTo(10_000L);
        assertThat(userAccountDaoV5.findByUserId(toUserId).getBalance()).isEqualTo(5_000L);
    }

    /**
     * 정확히 전액 이체: 경계값 - 잔액과 이체금액이 같을 때 성공해야 한다
     */
    @Test
    void accountTransfer_exactBalance_success() {
        userAccountServiceV5.accountTransfer(fromUserId, toUserId, 10_000L);

        assertThat(userAccountDaoV5.findByUserId(fromUserId).getBalance()).isEqualTo(0L);
        assertThat(userAccountDaoV5.findByUserId(toUserId).getBalance()).isEqualTo(15_000L);
    }
}
