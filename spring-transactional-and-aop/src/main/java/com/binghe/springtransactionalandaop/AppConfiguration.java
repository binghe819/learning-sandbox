package com.binghe.springtransactionalandaop;

import com.binghe.springtransactionalandaop.persistence.CustomerDao;
import com.binghe.springtransactionalandaop.persistence.CustomerDaoJdbc;
import com.binghe.springtransactionalandaop.service.TransactionProxyFactoryBean;
import com.binghe.springtransactionalandaop.service.TransferService;
import com.binghe.springtransactionalandaop.service.TransferServiceImpl;
import com.binghe.springtransactionalandaop.service.TransferServiceTx;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class AppConfiguration {

    @Bean
    public TransferService transferServiceImpl() {
        return new TransferServiceImpl(customerDao());
    }

//    @Bean
//    public TransferService transferServiceTx() {
//        return new TransferServiceTx(transferServiceImpl(), platformTransactionManager());
//    }

    @Bean
    public TransactionProxyFactoryBean transferService() {
        return new TransactionProxyFactoryBean(
                transferServiceImpl(),
                platformTransactionManager(),
                "transfer",
                TransferService.class
        );
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager() {
        return new DataSourceTransactionManager(dataSource());
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
