package com.binghe.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    private static final String URL = "jdbc:mysql://localhost:3306/spring-data-test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        config.setMaximumPoolSize(5);
        return new HikariDataSource(config);
    }

    /**
     * PlatformTransactionManager 빈을 등록한다.
     * @Transactional AOP 프록시(TransactionInterceptor)가 이 빈을 사용해
     * 트랜잭션 시작/커밋/롤백을 처리한다.
     *
     * 빈 이름을 "transactionManager"로 맞추면 @Transactional이 별도 지정 없이 자동으로 찾는다.
     *
     * @Configuration 클래스 내에서 같은 클래스의 다른 @Bean 메서드에 의존할 때는
     * 파라미터로 받지 않고 직접 메서드를 호출한다.
     * CGLIB 프록시가 AppConfig를 감싸고 있어 dataSource()를 여러 번 호출해도
     * 항상 동일한 싱글톤 빈을 반환하므로 중복 생성이 발생하지 않는다.
     */
    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
