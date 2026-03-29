package com.binghe.three_transactional.dao;

import com.binghe.domain.User;
import com.binghe.domain.UserAccount;
import com.binghe.three_transactional.AppConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
@Transactional
class UserAccountDaoV4Test {

    @Autowired
    private UserDaoV4 userDaoV4;

    @Autowired
    private UserAccountDaoV4 userAccountDaoV4;

    @Test
    void insert_후_findByUserId로_조회() throws SQLException {
        Long userId = userDaoV4.insert(new User(null, "binghe", "pw"));
        userAccountDaoV4.insert(new UserAccount(userId, 10000L));

        UserAccount account = userAccountDaoV4.findByUserId(userId);

        assertNotNull(account);
        assertEquals(userId, account.getUserId());
        assertEquals(10000L, account.getBalance());
    }

    @Test
    void update_후_잔액_변경_확인() throws SQLException {
        Long userId = userDaoV4.insert(new User(null, "binghe", "pw"));
        userAccountDaoV4.insert(new UserAccount(userId, 5000L));

        userAccountDaoV4.update(new UserAccount(userId, 9000L));

        UserAccount updated = userAccountDaoV4.findByUserId(userId);
        assertEquals(9000L, updated.getBalance());
    }

    @Test
    void delete_후_조회시_null() throws SQLException {
        Long userId = userDaoV4.insert(new User(null, "binghe", "pw"));
        userAccountDaoV4.insert(new UserAccount(userId, 5000L));

        userAccountDaoV4.delete(userId);

        assertNull(userAccountDaoV4.findByUserId(userId));
    }

    @Test
    void findByUserId_존재하지_않으면_null() throws SQLException {
        assertNull(userAccountDaoV4.findByUserId(Long.MAX_VALUE));
    }
}
