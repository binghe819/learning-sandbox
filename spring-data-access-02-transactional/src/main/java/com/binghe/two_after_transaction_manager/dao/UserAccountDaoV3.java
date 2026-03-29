package com.binghe.two_after_transaction_manager.dao;

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

public class UserAccountDaoV3 {

    private static final Logger log = LoggerFactory.getLogger(UserAccountDaoV3.class);

    private final DataSource dataSource;

    public UserAccountDaoV3(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * DataSourceUtils.getConnection()은 TransactionSynchronizationManager에
     * 현재 스레드에 바인딩된 커넥션이 있으면 그것을 반환하고, 없으면 새로 생성한다.
     */
    private Connection getConnection() throws SQLException {
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }

    /**
     * DataSourceUtils.releaseConnection()은 트랜잭션 중인 커넥션이면 닫지 않고 유지하며,
     * 트랜잭션이 없는 커넥션이면 풀에 반납한다.
     */
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
