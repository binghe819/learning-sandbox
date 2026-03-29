package com.binghe.dao;

import com.binghe.domain.User;
import com.binghe.domain.UserAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserAccountDaoTest {

    private static final ConnectionFactory connectionFactory = new ConnectionFactory();
    private static final UserDao userDao = new UserDao(connectionFactory);
    private static final UserAccountDao userAccountDao = new UserAccountDao(connectionFactory);

    private Long userId;

    @BeforeEach
    void setUp() throws SQLException {
        for (UserAccount userAccount : userAccountDao.findAll()) {
            userAccountDao.delete(userAccount.getUserId());
        }
        for (User user : userDao.findAll()) {
            userDao.delete(user.getId());
        }
        userId = userDao.insert(new User(null, "binghe", "1234"));
    }

    @Test
    void insert() throws SQLException {
        userAccountDao.insert(new UserAccount(userId, 1000L));

        UserAccount found = userAccountDao.findByUserId(userId);
        assertNotNull(found);
        assertEquals(userId, found.getUserId());
        assertEquals(1000L, found.getBalance());
    }

    @Test
    void findByUserId_존재하지_않으면_null_반환() throws SQLException {
        UserAccount found = userAccountDao.findByUserId(99999L);
        assertNull(found);
    }

    @Test
    void findAll() throws SQLException {
        Long userId2 = userDao.insert(new User(null, "binghe2", "1234"));
        userAccountDao.insert(new UserAccount(userId, 1000L));
        userAccountDao.insert(new UserAccount(userId2, 2000L));

        List<UserAccount> result = userAccountDao.findAll();
        assertEquals(2, result.size());
    }

    @Test
    void update() throws SQLException {
        userAccountDao.insert(new UserAccount(userId, 1000L));

        userAccountDao.update(new UserAccount(userId, 5000L));

        UserAccount found = userAccountDao.findByUserId(userId);
        assertNotNull(found);
        assertEquals(5000L, found.getBalance());
    }

    @Test
    void delete() throws SQLException {
        userAccountDao.insert(new UserAccount(userId, 1000L));

        userAccountDao.delete(userId);

        assertNull(userAccountDao.findByUserId(userId));
    }

    @Test
    void transfer_성공() throws SQLException {
        Long toUserId = userDao.insert(new User(null, "binghe2", "1234"));
        userAccountDao.insert(new UserAccount(userId, 10000L));
        userAccountDao.insert(new UserAccount(toUserId, 5000L));

        userAccountDao.transfer(userId, toUserId, 3000L);

        assertEquals(7000L, userAccountDao.findByUserId(userId).getBalance());
        assertEquals(8000L, userAccountDao.findByUserId(toUserId).getBalance());
    }

    @Test
    void transfer_잔액부족시_예외발생하고_롤백된다() throws SQLException {
        Long toUserId = userDao.insert(new User(null, "binghe2", "1234"));
        userAccountDao.insert(new UserAccount(userId, 1000L));
        userAccountDao.insert(new UserAccount(toUserId, 5000L));

        assertThrows(IllegalStateException.class,
                () -> userAccountDao.transfer(userId, toUserId, 5000L));

        // 롤백 확인 - 잔액이 변경되지 않아야 함
        assertEquals(1000L, userAccountDao.findByUserId(userId).getBalance());
        assertEquals(5000L, userAccountDao.findByUserId(toUserId).getBalance());
    }

    @Test
    void transfer_존재하지_않는_from_계좌이면_예외발생하고_롤백된다() throws SQLException {
        Long toUserId = userDao.insert(new User(null, "binghe2", "1234"));
        userAccountDao.insert(new UserAccount(toUserId, 5000L));

        assertThrows(IllegalStateException.class,
                () -> userAccountDao.transfer(99999L, toUserId, 1000L));

        // 롤백 확인 - to 잔액이 변경되지 않아야 함
        assertEquals(5000L, userAccountDao.findByUserId(toUserId).getBalance());
    }

    @Test
    void transfer_존재하지_않는_to_계좌이면_예외발생하고_롤백된다() throws SQLException {
        userAccountDao.insert(new UserAccount(userId, 10000L));

        assertThrows(IllegalStateException.class,
                () -> userAccountDao.transfer(userId, 99999L, 1000L));

        // 롤백 확인 - from 잔액이 변경되지 않아야 함
        assertEquals(10000L, userAccountDao.findByUserId(userId).getBalance());
    }
}
