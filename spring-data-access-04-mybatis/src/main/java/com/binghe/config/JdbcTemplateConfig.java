package com.binghe.config;

import com.binghe.repository.ItemRepository;
import com.binghe.repository.jdbctemplate.JdbcTemplateItemRepositoryV1;
import com.binghe.repository.jdbctemplate.JdbcTemplateItemRepositoryV2;
import com.binghe.repository.jdbctemplate.JdbcTemplateItemRepositoryV3;
import com.binghe.service.ItemService;
import com.binghe.service.ItemServiceV1;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

/**
 * @Import는 다른 @Configuration 클래스를 현재 설정 클래스에 포함시키는 어노테이션이다.
 *
 * 동작 방식:
 * Spring 컨테이너가 JdbcTemplateConfig를 처리할 때, @Import에 명시된
 * DataSourceConfig도 함께 읽어 해당 클래스의 @Bean 메서드들을 동일한
 * ApplicationContext에 등록한다.
 *
 * 즉, @Import(DataSourceConfig.class)는 아래 두 가지를 직접 선언한 것과 동일하다:
 *   1. DataSourceConfig 클래스를 @Configuration으로 등록
 *   2. DataSourceConfig 내부의 @Bean(getDataSource)을 컨테이너에 등록
 *
 * 이를 통해 itemRepository(DataSource dataSource) 파라미터에
 * DataSourceConfig에서 등록한 DataSource 빈이 자동으로 주입된다.
 *
 * @Import를 사용하는 이유:
 * @ComponentScan을 사용하면 classpath 전체를 탐색하지만,
 * @Import는 필요한 설정 클래스만 명시적으로 가져오므로
 * 테스트나 모듈 단위 구성 시 컨텍스트 범위를 의도적으로 제한할 수 있다.
 * (예: 테스트에서 @ContextConfiguration(classes = JdbcTemplateConfig.class) 하나만
 *  지정해도 DataSourceConfig까지 함께 로드된다.)
 */
@Import(DataSourceConfig.class)
@Configuration
public class JdbcTemplateConfig {

    @Bean
    public ItemRepository itemRepository(DataSource dataSource) {
        return new JdbcTemplateItemRepositoryV3(dataSource);
//        return new JdbcTemplateItemRepositoryV2(dataSource);
//        return new JdbcTemplateItemRepositoryV1(dataSource);
    }

    @Bean
    public ItemService itemService(ItemRepository itemRepository) {
        return new ItemServiceV1(itemRepository);
    }
}
