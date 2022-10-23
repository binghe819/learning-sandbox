package com.binghe.springtransactionalandaop;

import com.binghe.springtransactionalandaop.persistence.CustomerDao;
import com.binghe.springtransactionalandaop.persistence.CustomerDaoJdbc;
import com.binghe.springtransactionalandaop.service.TransferService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class AppConfiguration {

    @Bean
    public TransferService transferService() {
        return new TransferService(customerDao(), dataSource());
    }

    @Bean
    public CustomerDao customerDao() {
        return new CustomerDaoJdbc(dataSource());
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:13306/toby");
        dataSource.setUsername("root");
        dataSource.setPassword("passwd");
        return dataSource;
    }
}
