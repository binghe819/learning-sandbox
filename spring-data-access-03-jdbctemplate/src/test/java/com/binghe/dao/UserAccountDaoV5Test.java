package com.binghe.dao;

import com.binghe.AppConfig;
import com.binghe.domain.User;
import com.binghe.domain.UserAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 실제 MySQL DB를 사용하는 통합 테스트.
 * user_accounts.user_id는 users.id를 FK로 참조하므로, 먼저 User를 삽입한 뒤 UserAccount를 조작한다.
 * @Transactional로 각 테스트 후 자동 롤백된다.
 */
@SpringJUnitConfig(AppConfig.class)
@Transactional
class UserAccountDaoV5Test {

    @Autowired
    private UserDaoV5 userDaoV5;

    @Autowired
    private UserAccountDaoV5 userAccountDaoV5;

    @Test
    void insert_findByUserId() {
        Long userId = userDaoV5.insert(new User(null, "dave", "pw"));
        UserAccount account = new UserAccount(userId, 10_000L);

        userAccountDaoV5.insert(account);

        UserAccount found = userAccountDaoV5.findByUserId(userId);
        assertThat(found).isNotNull();
        assertThat(found.getBalance()).isEqualTo(10_000L);
    }

    @Test
    void update() {
        Long userId = userDaoV5.insert(new User(null, "eve", "pw"));
        userAccountDaoV5.insert(new UserAccount(userId, 5_000L));

        userAccountDaoV5.update(new UserAccount(userId, 9_999L));

        assertThat(userAccountDaoV5.findByUserId(userId).getBalance()).isEqualTo(9_999L);
    }

    @Test
    void delete() {
        Long userId = userDaoV5.insert(new User(null, "frank", "pw"));
        userAccountDaoV5.insert(new UserAccount(userId, 3_000L));

        userAccountDaoV5.delete(userId);

        assertThat(userAccountDaoV5.findByUserId(userId)).isNull();
    }

    @Test
    void findByUserId_notFound_returnsNull() {
        assertThat(userAccountDaoV5.findByUserId(-1L)).isNull();
    }
}
