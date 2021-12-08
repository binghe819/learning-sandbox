package com.binghe.springbootdbreplicationjpa.config;

import com.binghe.springbootdbreplicationjpa.config.DataSourceProperties.DatasourceNode;
import com.zaxxer.hikari.HikariDataSource;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DataSourceProperties.class)
@Configuration
public class ReplicationDataSourceConfiguration {

    private final DataSourceProperties dataSourceProperties;
    private final JpaProperties jpaProperties;

    public ReplicationDataSourceConfiguration(
        DataSourceProperties dataSourceProperties,
        JpaProperties jpaProperties
    ) {
        this.dataSourceProperties = dataSourceProperties;
        this.jpaProperties = jpaProperties;
    }

    @Bean
    public DataSource routingDataSource() {

        // DataSource 설정
        Map<Object, Object> dataSources = new HashMap<>();
        DataSource master = createDataSource(dataSourceProperties.getMaster());
        dataSources.put("master", master);
        dataSourceProperties.getSlave().forEach((key, value) ->
            dataSources.put(key, createDataSource(value))
        );

        // 라운드 로빈 방식의 DataSource 정의
        ReplicationRoutingDataSource replicationRoutingDataSource
            = new ReplicationRoutingDataSource();
        replicationRoutingDataSource.setDefaultTargetDataSource(master);
        replicationRoutingDataSource.setTargetDataSources(dataSources);
        return replicationRoutingDataSource;
    }

    private DataSource createDataSource(DatasourceNode datasourceNode) {
        return DataSourceBuilder.create()
            .type(HikariDataSource.class)
            .url(datasourceNode.getUrl())
            .driverClassName("com.mysql.cj.jdbc.Driver")
            .username(datasourceNode.getUsername())
            .password(datasourceNode.getPassword())
            .build();
    }

    // 실제 쿼리가 실행될 때마다 DataSource 가져오도록 Lazy로 빈 등록
    @Bean
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDataSource());
    }

    // JPA에서 사용되는 EntityManagerFactory 설정 (LazyConnectionDataSourceProxy를 넣기 위함)
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        EntityManagerFactoryBuilder entityManagerFactoryBuilder = createEntityManagerFactoryBuilder(jpaProperties);
        return entityManagerFactoryBuilder.dataSource(dataSource())
            .packages("com.binghe.springbootdbreplication") // 프로젝트 패키지 경로
            .build();
    }

    private EntityManagerFactoryBuilder createEntityManagerFactoryBuilder(JpaProperties jpaProperties) {
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        return new EntityManagerFactoryBuilder(vendorAdapter, jpaProperties.getProperties(), null);
    }

    // JPA에서 사용할 TransactionManager 설정 (entityManagerFactory로 인해 정의해준다)
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager tm = new JpaTransactionManager();
        tm.setEntityManagerFactory(entityManagerFactory);
        return tm;
    }
}
