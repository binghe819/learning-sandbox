package com.binghe.springtransactionalandaop;

import com.binghe.springtransactionalandaop.persistence.CustomerDao;
import com.binghe.springtransactionalandaop.persistence.CustomerDaoJdbc;
import com.binghe.springtransactionalandaop.service.TransactionMethodInterceptor;
import com.binghe.springtransactionalandaop.service.TransferService;
import com.binghe.springtransactionalandaop.service.TransferServiceImpl;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
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

    // 어드바이스 설정
    @Bean
    public TransactionMethodInterceptor transactionAdvice() {
        return new TransactionMethodInterceptor(platformTransactionManager());
    }

    // 포인트 컷 설정
    @Bean
    public NameMatchMethodPointcut transactionPointcut() {
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("transfer");
        return pointcut;
    }

    // 어드바이저 빈 설정
    @Bean
    public DefaultPointcutAdvisor transactionAdvisor() {
        return new DefaultPointcutAdvisor(transactionPointcut(), transactionAdvice());
    }

    @Bean
    public TransferService transferService() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(transferServiceImpl());
        pfBean.addAdvisor(transactionAdvisor());
        return (TransferService) pfBean.getObject();
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
