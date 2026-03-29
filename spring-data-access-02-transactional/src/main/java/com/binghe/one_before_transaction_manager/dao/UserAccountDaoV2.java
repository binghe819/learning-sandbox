package com.binghe.one_before_transaction_manager.dao;

import com.binghe.domain.UserAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserAccountDaoV2 {

    public void insert(Connection conn, UserAccount userAccount) throws SQLException {
        String sql = "INSERT INTO user_accounts (user_id, balance) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userAccount.getUserId());
            ps.setLong(2, userAccount.getBalance());
            ps.executeUpdate();
        }
    }

    public UserAccount findByUserId(Connection conn, Long userId) throws SQLException {
        String sql = "SELECT user_id, balance FROM user_accounts WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserAccount(rs.getLong("user_id"), rs.getLong("balance"));
                }
                return null;
            }
        }
    }

    public List<UserAccount> findAll(Connection conn) throws SQLException {
        String sql = "SELECT user_id, balance FROM user_accounts";
        List<UserAccount> result = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new UserAccount(rs.getLong("user_id"), rs.getLong("balance")));
            }
        }
        return result;
    }

    public void update(Connection conn, UserAccount userAccount) throws SQLException {
        String sql = "UPDATE user_accounts SET balance = ? WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userAccount.getBalance());
            ps.setLong(2, userAccount.getUserId());
            ps.executeUpdate();
        }
    }

    public void delete(Connection conn, Long userId) throws SQLException {
        String sql = "DELETE FROM user_accounts WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }
}
