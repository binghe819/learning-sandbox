package com.binghe.dao;

import com.binghe.domain.User;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {

    private static final ConnectionFactory connectionFactory = new ConnectionFactory();
    private static final UserDao userDao = new UserDao(connectionFactory);

    @BeforeEach
    void setUp() throws SQLException {
        for (User user : userDao.findAll()) {
            userDao.delete(user.getId());
        }
    }

    @Test
    void insert_생성된_ID를_반환한다() throws SQLException {
        Long id = userDao.insert(new User(null, "binghe", "1234"));

        assertNotNull(id);
        assertTrue(id > 0);
        System.out.println(id);
    }

    @Test
    void findById() throws SQLException {
        Long id = userDao.insert(new User(null, "binghe", "1234"));

        User found = userDao.findById(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
        assertEquals("binghe", found.getName());
        assertEquals("1234", found.getPassword());
    }

    @Test
    void findById_존재하지_않으면_null_반환() throws SQLException {
        User found = userDao.findById(99999L);
        assertNull(found);
    }

    @Test
    void findAll() throws SQLException {
        Long id1 = userDao.insert(new User(null, "binghe", "1234"));
        Long id2 = userDao.insert(new User(null, "binghe2", "5678"));

        List<User> users = userDao.findAll();
        assertEquals(2, users.size());
        assertTrue(users.stream().anyMatch(u -> u.getId().equals(id1)));
        assertTrue(users.stream().anyMatch(u -> u.getId().equals(id2)));
    }

    @Test
    void update() throws SQLException {
        Long id = userDao.insert(new User(null, "binghe", "1234"));

        userDao.update(new User(id, "binghe-updated", "5678"));

        User found = userDao.findById(id);
        assertNotNull(found);
        assertEquals("binghe-updated", found.getName());
        assertEquals("5678", found.getPassword());
    }

    @Test
    void delete() throws SQLException {
        Long id = userDao.insert(new User(null, "binghe", "1234"));

        userDao.delete(id);

        assertNull(userDao.findById(id));
    }
}
