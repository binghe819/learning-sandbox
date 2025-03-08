package com.binghe.integration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestDatabaseConfig implements InitializingBean {

    private final DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final RedisConnectionFactory redisConnectionFactory;

    private List<String> tableNames;

    public TestDatabaseConfig(DataSource dataSource, JdbcTemplate jdbcTemplate, RedisConnectionFactory redisConnectionFactory) {
        this.dataSource = dataSource;
        this.jdbcTemplate = jdbcTemplate;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS Members (\n" +
                "    id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                "    name VARCHAR(255) NOT NULL,\n" +
                "    address VARCHAR(255),\n" +
                "    description TEXT\n" +
                ");\n";

        jdbcTemplate.execute("DROP TABLE IF EXISTS Members");
        jdbcTemplate.execute(sql);
        System.out.println("Database table 'members' has been created (if not exists). schema: " + sql);

        try (Connection connection = dataSource.getConnection()) {
            System.out.println("테이블 찾자");
            tableNames = extractTableNames(connection);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    // 모든 테이블 이름 추출
    private List<String> extractTableNames(Connection conn) throws SQLException {
        List<String> tableNames = new ArrayList<>();

        try (ResultSet tables = conn.getMetaData()
                .getTables(null, "PUBLIC", "%", new String[]{"TABLE"})
        ) {
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                System.out.println("tableName: " + tableName);
                tableNames.add(tableName);
            }

            return tableNames;
        }
    }

    public void clear() {
        try (Connection connection = dataSource.getConnection()) {
            cleanUpDatabase(connection);
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }

    // 모든 테이블 데이터 제거
    private void cleanUpDatabase(Connection conn) throws SQLException {
        flushRedis();

        try (Statement statement = conn.createStatement()) {

            // 데이터 무결성 설정 OFF
            statement.executeUpdate("SET REFERENTIAL_INTEGRITY FALSE");

            for (String tableName : tableNames) {
                try {
                    // TRUNCATE
                    System.out.println("TRUNCATED: " + tableName);
                    statement.executeUpdate("TRUNCATE TABLE " + tableName);
                    statement.executeUpdate("ALTER TABLE " + tableName + " ALTER COLUMN id RESTART WITH 1;"); // AI reset하기 위함.
                } catch (SQLException ignore) {
                }
            }

            // 데이터 무결성 설정 ON
            statement.executeUpdate("SET REFERENTIAL_INTEGRITY TRUE");
        }
    }

    private void flushRedis() {
        try (RedisConnection redisConnection = redisConnectionFactory.getConnection()) {
            redisConnection.serverCommands().flushAll();
        }
    }
}
