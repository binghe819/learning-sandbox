package com.binghe.two_after_transaction_manager.dao;

import com.binghe.domain.User;
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

public class UserDaoV3 {

    private static final Logger log = LoggerFactory.getLogger(UserDaoV3.class);

    private final DataSource dataSource;

    public UserDaoV3(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * DataSourceUtils.getConnection()은 트랜잭션 동기화 매니저(TransactionSynchronizationManager)에
     * 현재 스레드에 바인딩된 커넥션이 있으면 그것을 반환하고, 없으면 새로 생성한다.
     *
     * 덕분에 Service에서 시작한 트랜잭션이 DAO 내부의 같은 커넥션에서 이어진다.
     * (DataSource.getConnection()을 직접 호출하면 매번 새 커넥션을 받아 트랜잭션이 끊긴다.)
     */
    private Connection getConnection() throws SQLException {
        Connection con = DataSourceUtils.getConnection(dataSource);
        log.info("get connection={}, class={}", con, con.getClass());
        return con;
    }

    /**
     * DataSourceUtils.releaseConnection()은 트랜잭션 동기화 매니저에 바인딩된 커넥션이면
     * 실제로 닫지 않고 유지한다. 트랜잭션이 없는 커넥션이면 풀에 반납(close)한다.
     *
     * 덕분에 트랜잭션 범위 내에서는 같은 커넥션이 계속 살아있고,
     * 트랜잭션이 끝난 뒤 TransactionManager가 최종적으로 커넥션을 닫는다.
     */
    private void close(Connection con, Statement stmt, ResultSet rs) {
        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        DataSourceUtils.releaseConnection(con, dataSource);
    }

    public Long insert(User user) throws SQLException {
        String sql = "INSERT INTO users (name, password) VALUES (?, ?)";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.executeUpdate();
            generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            }
            throw new SQLException("insert 후 생성된 ID를 가져오지 못했습니다.");
        } finally {
            close(con, ps, generatedKeys);
        }
    }

    public User findById(Long id) throws SQLException {
        String sql = "SELECT id, name, password FROM users WHERE id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getLong("id"), rs.getString("name"), rs.getString("password"));
            }
            return null;
        } finally {
            close(con, ps, rs);
        }
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, password = ? WHERE id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setLong(3, user.getId());
            ps.executeUpdate();
        } finally {
            close(con, ps, null);
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            ps.executeUpdate();
        } finally {
            close(con, ps, null);
        }
    }
}
