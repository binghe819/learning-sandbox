package com.binghe;

import com.binghe.dao.UserAccountDaoV5;
import com.binghe.dao.UserDaoV5;
import com.binghe.service.UserAccountServiceV5;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @EnableTransactionManagement:
 * Spring 컨테이너에 @Transactional 어노테이션을 인식하는 AOP 인프라를 등록한다.
 * 구체적으로는 TransactionInterceptor(어드바이스)와 포인트컷을 빈으로 등록하여,
 * @Transactional이 붙은 메서드를 가진 빈을 자동으로 프록시로 감싼다.
 *
 * Spring Boot를 사용하면 @EnableTransactionManagement와 DataSourceTransactionManager 빈이
 * 자동 설정(AutoConfiguration)으로 등록되므로 이 설정 클래스가 필요 없다.
 */
@Configuration
@EnableTransactionManagement
public class AppConfig {

    @Bean
    public DataSource dataSource() {
        return new DataSourceFactory().getDataSource();
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
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public UserDaoV5 userDaoV5(DataSource dataSource) {
        return new UserDaoV5(dataSource);
    }

    @Bean
    public UserAccountDaoV5 userAccountDaoV5(DataSource dataSource) {
        return new UserAccountDaoV5(dataSource);
    }

    @Bean
    public UserAccountServiceV5 userAccountServiceV5(UserDaoV5 userDaoV5, UserAccountDaoV5 userAccountDaoV5) {
        return new UserAccountServiceV5(userDaoV5, userAccountDaoV5);
    }
}
