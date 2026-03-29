package com.binghe.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DataSource는 Connection을 반환하는 인터페이스다.
 * 커넥션을 매번 새로 만들든 커넥션 풀을 사용하든, 그 방식은 구현체에 따라 다르게 동작한다.
 *
 * 애플리케이션 코드(UserDao 등)는 DataSource 타입에만 의존하면 되고,
 * 구현체가 무엇인지 알 필요가 없다. 따라서 구현체를 교체해도 애플리케이션 코드는 변경이 없다.
 * Spring이 DataSource 빈을 주입해주는 방식도 이 원리를 그대로 활용한다.
 *
 * DataSource (인터페이스)
 * ├── MysqlDataSource  → getConnection() 호출 시 매번 새 TCP 연결 생성 (DriverManager와 동일한 동작)
 * └── HikariDataSource → getConnection() 호출 시 풀에서 borrow
 */
public class ConnectionFactory {

    private static final Logger log = LoggerFactory.getLogger(ConnectionFactory.class);

    private static final String URL = "jdbc:mysql://localhost:3306/spring-data-test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    private final HikariDataSource hikariDataSource;

    public ConnectionFactory() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(5);
        hikariDataSource = new HikariDataSource(config);
    }

    /**
     * DriverManager.getConnection()은 호출할 때마다 MySQL과 새로운 TCP 연결을 맺고 Connection을 반환한다.
     * 즉, 커넥션 풀 없이 매 요청마다 TCP 핸드셰이크 + 인증 과정이 발생한다.
     * try-with-resources 등으로 Connection을 close()하면 풀에 반납되는 게 아니라 실제 TCP 연결이 종료된다.
     */
    public Connection getNewConnection() throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
            log.info("get connection={}, class={}", connection, connection.getClass());
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * HikariCP 커넥션 풀에서 커넥션을 borrow해서 반환한다.
     * 풀에 여유 커넥션이 있으면 즉시 반환하고, 없으면 커넥션이 반납될 때까지 대기한다.
     * try-with-resources 등으로 Connection을 close()하면 실제 연결을 끊는 게 아니라 풀에 반납된다.
     * 최대 커넥션 수: 5개
     */
    public Connection getConnectionFromConnectionPool() throws SQLException {
        Connection connection = hikariDataSource.getConnection();
        log.info("get connection={}, class={}", connection, connection.getClass());
        return connection;
    }
}
