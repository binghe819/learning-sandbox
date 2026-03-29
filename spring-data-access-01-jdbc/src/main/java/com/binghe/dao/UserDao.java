package com.binghe.dao;

import com.binghe.domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {

    private final ConnectionFactory connectionFactory;

    public UserDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Long insert(User user) throws SQLException {
        String sql = "INSERT INTO users (name, password) VALUES (?, ?)";
        try (Connection conn = connectionFactory.getConnectionFromConnectionPool();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.executeUpdate();
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
                throw new SQLException("insert 후 생성된 ID를 가져오지 못했습니다.");
            }
        }
    }

    public User findById(Long id) throws SQLException {
        String sql = "SELECT id, name, password FROM users WHERE id = ?";
        try (Connection conn = connectionFactory.getConnectionFromConnectionPool();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getLong("id"), rs.getString("name"), rs.getString("password"));
                }
                return null;
            }
        }
    }

    public List<User> findAll() throws SQLException {
        String sql = "SELECT id, name, password FROM users";
        List<User> users = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnectionFromConnectionPool();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                users.add(new User(rs.getLong("id"), rs.getString("name"), rs.getString("password")));
            }
        }
        return users;
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET name = ?, password = ? WHERE id = ?";
        try (Connection conn = connectionFactory.getConnectionFromConnectionPool();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getPassword());
            ps.setLong(3, user.getId());
            ps.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = connectionFactory.getConnectionFromConnectionPool();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
}
