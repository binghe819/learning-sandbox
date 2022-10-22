package com.binghe.springtransactionalandaop;

import com.binghe.springtransactionalandaop.persistence.CustomerDao;
import com.binghe.springtransactionalandaop.persistence.CustomerDaoJdbcTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class AppConfiguration {

    @Bean
    public CustomerDao customerDao() {
        return new CustomerDaoJdbcTemplate(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:13306/toby");
        dataSource.setUsername("root");
        dataSource.setPassword("passwd");
        return dataSource;
    }
}
