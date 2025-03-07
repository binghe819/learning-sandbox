package com.binghe.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;

//public class TestDatabaseSchemeInitializer implements ApplicationRunner {
//
//    private final JdbcTemplate jdbcTemplate;
//
//    public TestDatabaseSchemeInitializer(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        String sql = "CREATE TABLE IF NOT EXISTS Members (\n" +
//                "    id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
//                "    name VARCHAR(255) NOT NULL,\n" +
//                "    address VARCHAR(255),\n" +
//                "    description TEXT\n" +
//                ");\n";
//
//        jdbcTemplate.execute("DROP TABLE IF EXISTS Members");
//        jdbcTemplate.execute(sql);
//        System.out.println("Database table 'members' has been created (if not exists). schema: " + sql);
//    }
//}
