package com.binghe.dao;

import com.binghe.domain.UserAccount;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAccountDao {

    private final ConnectionFactory connectionFactory;

    public UserAccountDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public void insert(UserAccount userAccount) throws SQLException {
        String sql = "INSERT INTO user_accounts (user_id, balance) VALUES (?, ?)";
        try (Connection conn = connectionFactory.getConnectionFromConnectionPool();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userAccount.getUserId());
            ps.setLong(2, userAccount.getBalance());
            ps.executeUpdate();
        }
    }

    public UserAccount findByUserId(Long userId) throws SQLException {
        String sql = "SELECT user_id, balance FROM user_accounts WHERE user_id = ?";
        try (Connection conn = connectionFactory.getConnectionFromConnectionPool();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserAccount(rs.getLong("user_id"), rs.getLong("balance"));
                }
                return null;
            }
        }
    }

    public List<UserAccount> findAll() throws SQLException {
        String sql = "SELECT user_id, balance FROM user_accounts";
        List<UserAccount> result = new ArrayList<>();
        try (Connection conn = connectionFactory.getConnectionFromConnectionPool();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                result.add(new UserAccount(rs.getLong("user_id"), rs.getLong("balance")));
            }
        }
        return result;
    }

    public void update(UserAccount userAccount) throws SQLException {
        String sql = "UPDATE user_accounts SET balance = ? WHERE user_id = ?";
        try (Connection conn = connectionFactory.getConnectionFromConnectionPool();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userAccount.getBalance());
            ps.setLong(2, userAccount.getUserId());
            ps.executeUpdate();
        }
    }

    public void delete(Long userId) throws SQLException {
        String sql = "DELETE FROM user_accounts WHERE user_id = ?";
        try (Connection conn = connectionFactory.getConnectionFromConnectionPool();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        }
    }

    /**
     * <여기부터 아래는 돈 송금 로직. @Transctional 없이 DAO 단에서 트랜잭션을 묶는 예시>
     */
    public void transfer(Long from, Long to, Long amount) throws SQLException {
        try (Connection conn = connectionFactory.getConnectionFromConnectionPool()) {
            conn.setAutoCommit(false);
            try {
                UserAccount fromAccount = findByUserIdWithConn(conn, from);
                if (fromAccount.getBalance() < amount) {
                    throw new IllegalStateException("잔액이 부족합니다. 현재 잔액: " + fromAccount.getBalance());
                }

                updateBalanceWithConn(conn, from, fromAccount.getBalance() - amount);
                UserAccount toAccount = findByUserIdWithConn(conn, to);
                updateBalanceWithConn(conn, to, toAccount.getBalance() + amount);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    private UserAccount findByUserIdWithConn(Connection conn, Long userId) throws SQLException {
        String sql = "SELECT user_id, balance FROM user_accounts WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new UserAccount(rs.getLong("user_id"), rs.getLong("balance"));
                }
                throw new IllegalStateException("계좌를 찾을 수 없습니다. userId: " + userId);
            }
        }
    }

    private void updateBalanceWithConn(Connection conn, Long userId, Long balance) throws SQLException {
        String sql = "UPDATE user_accounts SET balance = ? WHERE user_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, balance);
            ps.setLong(2, userId);
            ps.executeUpdate();
        }
    }
}
