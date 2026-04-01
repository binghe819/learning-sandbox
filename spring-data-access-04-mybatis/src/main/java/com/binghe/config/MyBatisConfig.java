package com.binghe.config;

import com.binghe.repository.ItemRepository;
import com.binghe.repository.mybatis.ItemMapper;
import com.binghe.repository.mybatis.MyBatisItemRepository;
import com.binghe.service.ItemService;
import com.binghe.service.ItemServiceV2;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Import(DataSourceConfig.class)
@MapperScan("com.binghe.repository.mybatis") // @Mapper 인터페이스를 스캔해 Spring 빈으로 등록한다.
@org.springframework.context.annotation.Configuration
public class MyBatisConfig {

    /**
     * MyBatis의 핵심 팩토리 빈이다.
     * DataSource와 매퍼 XML 위치, 타입 별칭 패키지를 설정하면
     * SqlSessionFactory를 생성해 컨테이너에 등록한다.
     *
     * - setTypeAliasesPackage: 해당 패키지의 클래스명을 XML resultType에서
     *   풀 패키지명 없이 짧은 이름(Item)으로 사용할 수 있게 한다.
     * - setMapperLocations: 매퍼 XML 파일의 classpath 위치를 지정한다.
     *   @MapperScan만으로는 XML을 찾지 못하므로 명시적으로 등록해야 한다.
     * - mapUnderscoreToCamelCase: DB 컬럼의 snake_case(item_name)를
     *   자바 필드의 camelCase(itemName)로 자동 변환한다.
     *   이 옵션이 없으면 item_name 컬럼이 itemName 필드에 매핑되지 않아 null이 된다.
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setTypeAliasesPackage("com.binghe.domain");
        factoryBean.setMapperLocations(
                new PathMatchingResourcePatternResolver()
                        .getResources("classpath:com.binghe.repository.mybatis/*.xml")
        );

        // snake_case → camelCase 자동 변환 (item_name → itemName)
        Configuration mybatisConfig = new Configuration();
        mybatisConfig.setMapUnderscoreToCamelCase(true);
        factoryBean.setConfiguration(mybatisConfig);

        return factoryBean.getObject();
    }

    /**
     * @MapperScan이 ItemMapper 프록시 빈을 자동 등록하므로
     * 파라미터로 주입받아 MyBatisItemRepository에 전달한다.
     */
    @Bean
    public ItemRepository itemRepository(ItemMapper itemMapper) {
        return new MyBatisItemRepository(itemMapper);
    }

    @Bean
    public ItemService itemService(ItemRepository itemRepository) {
        return new ItemServiceV2(itemRepository);
    }
}
