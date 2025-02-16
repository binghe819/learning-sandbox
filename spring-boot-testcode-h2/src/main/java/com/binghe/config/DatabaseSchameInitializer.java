package com.binghe.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseSchameInitializer implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String sql = "CREATE TABLE IF NOT EXISTS Members (\n" +
                "    id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                "    name VARCHAR(255) NOT NULL,\n" +
                "    address VARCHAR(255),\n" +
                "    description TEXT\n" +
                ");\n";

        jdbcTemplate.execute(sql);
        log.info("Database table 'members' has been created (if not exists). schema: {}", sql);
    }
}
