package com.binghe.three_transactional.dao;

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

/**
 * V3와 구현이 동일하다. DataSourceUtils로 트랜잭션 동기화에 참여하는 방식은 변하지 않는다.
 * @Transactional은 서비스 계층에서 선언하며, DAO는 그 트랜잭션에 자동으로 참여한다.
 */
public class UserDaoV4 {

    private static final Logger log = LoggerFactory.getLogger(UserDaoV4.class);

    private final DataSource dataSource;

    public UserDaoV4(DataSource dataSource) {
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
