package com.binghe.three_transactional.dao;

import com.binghe.domain.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * V3와 구현이 동일하다. DataSourceUtils로 트랜잭션 동기화에 참여하는 방식은 변하지 않는다.
 * @Transactional은 서비스 계층에서 선언하며, DAO는 그 트랜잭션에 자동으로 참여한다.
 */
public class UserAccountDaoV4 {

    private static final Logger log = LoggerFactory.getLogger(UserAccountDaoV4.class);

    private final DataSource dataSource;

    public UserAccountDaoV4(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Connection getConnection() throws SQLException {
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        DataSourceUtils.releaseConnection(con, dataSource);
    }

    public void insert(UserAccount userAccount) throws SQLException {
        String sql = "INSERT INTO user_accounts (user_id, balance) VALUES (?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, userAccount.getUserId());
            ps.setLong(2, userAccount.getBalance());
            ps.executeUpdate();
        } finally {
            close(con, ps, null);
        }
    }

    public UserAccount findByUserId(Long userId) throws SQLException {
        String sql = "SELECT user_id, balance FROM user_accounts WHERE user_id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new UserAccount(rs.getLong("user_id"), rs.getLong("balance"));
            }
            return null;
        } finally {
            close(con, ps, rs);
        }
    }

    public void update(UserAccount userAccount) throws SQLException {
        String sql = "UPDATE user_accounts SET balance = ? WHERE user_id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, userAccount.getBalance());
            ps.setLong(2, userAccount.getUserId());
            ps.executeUpdate();
        } finally {
            close(con, ps, null);
        }
    }

    public void delete(Long userId) throws SQLException {
        String sql = "DELETE FROM user_accounts WHERE user_id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, userId);
            ps.executeUpdate();
        } finally {
            close(con, ps, null);
        }
    }
}
