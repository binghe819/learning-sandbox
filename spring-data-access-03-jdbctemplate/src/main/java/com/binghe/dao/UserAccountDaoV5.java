package com.binghe.dao;

import com.binghe.domain.UserAccount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class UserAccountDaoV5 {

    private final JdbcTemplate jdbcTemplate;

    public UserAccountDaoV5(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private RowMapper<UserAccount> userAccountRowMapper() {
        return (rs, rowNum) -> new UserAccount(
                rs.getLong("user_id"),
                rs.getLong("balance")
        );
    }

    public void insert(UserAccount userAccount) {
        String sql = "INSERT INTO user_accounts (user_id, balance) VALUES (?, ?)";
        jdbcTemplate.update(sql, userAccount.getUserId(), userAccount.getBalance());
    }

    public UserAccount findByUserId(Long userId) {
        String sql = "SELECT user_id, balance FROM user_accounts WHERE user_id = ?";
        List<UserAccount> results = jdbcTemplate.query(sql, userAccountRowMapper(), userId);
        return results.isEmpty() ? null : results.get(0);
    }

    public void update(UserAccount userAccount) {
        String sql = "UPDATE user_accounts SET balance = ? WHERE user_id = ?";
        jdbcTemplate.update(sql, userAccount.getBalance(), userAccount.getUserId());
    }

    public void delete(Long userId) {
        String sql = "DELETE FROM user_accounts WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }
}
