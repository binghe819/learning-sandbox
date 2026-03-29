package com.binghe.two_after_transaction_manager;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceFactory {

    private static final String URL = "jdbc:mysql://localhost:3306/spring-data-test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    private final HikariDataSource hikariDataSource;

    public DataSourceFactory() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(5);
        hikariDataSource = new HikariDataSource(config);
    }

    public DataSource getDataSource() {
        return hikariDataSource;
    }
}
