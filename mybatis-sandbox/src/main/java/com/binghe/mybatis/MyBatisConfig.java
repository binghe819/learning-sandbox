package com.binghe.mybatis;

import com.binghe.domain.Item;
import com.binghe.repository.ItemMapper;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;

public class MyBatisConfig {

    private static final String URL = "jdbc:mysql://localhost:3306/spring-data-test";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1234";

    public SqlSessionFactory sqlSessionFactory() {
        // DataSource: DB 커넥션 풀을 관리한다.
        // MyBatis는 SQL 실행 시 DataSource에서 커넥션을 빌려 쓰고, 완료 후 반납한다.
        DataSource dataSource = dataSource();

        // JdbcTransactionFactory: SqlSession 단위의 트랜잭션을 JDBC 방식으로 관리한다.
        // openSession() 호출 시 이 팩토리가 JdbcTransaction을 생성하며,
        // JdbcTransaction은 커넥션의 auto-commit을 false로 설정하고
        // commit()/rollback() 호출을 JDBC Connection에 직접 위임한다.
        // (Spring과 함께 쓸 때는 SpringManagedTransactionFactory를 사용해 Spring의 트랜잭션에 참여한다.)
        JdbcTransactionFactory jdbcTransactionFactory = new JdbcTransactionFactory();

        // Environment: MyBatis가 어떤 DB에, 어떤 트랜잭션 전략으로 접근할지를 묶은 실행 환경이다.
        // 첫 번째 인자("test")는 환경 식별자로, mybatis-config.xml 방식에서 여러 환경(dev/test/prod)을
        // 전환할 때 사용한다. 코드 방식에서는 단순 레이블 역할을 한다.
        Environment environment = new Environment("test", jdbcTransactionFactory, dataSource);

        Configuration configuration = new Configuration(environment);
        // "Item" 별칭을 등록: mapper XML의 resultType="Item" 이 com.binghe.domain.Item 으로 해석된다.
        // 등록하지 않으면 MyBatis는 "Item"을 클래스 풀네임으로 간주해 ClassNotFoundException을 던진다.
        configuration.getTypeAliasRegistry().registerAlias("Item", Item.class);
        // DB의 snake_case 컬럼명(item_name)을 Java의 camelCase 필드명(itemName)으로 자동 변환한다.
        configuration.setMapUnderscoreToCamelCase(true);
        configuration.addMapper(ItemMapper.class);
        registerMapperXml(configuration, "com.binghe.repository/itemMapper.xml");

        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        return sqlSessionFactory;
    }

    /**
     * Mapper XML 파일을 읽어 Configuration에 SQL 구문을 등록한다.
     *
     * [Resources.getResourceAsStream]
     *   MyBatis가 제공하는 유틸리티 클래스.
     *   내부적으로 Thread.currentThread().getContextClassLoader()를 사용해
     *   classpath(= src/main/resources) 기준으로 파일을 InputStream으로 로드한다.
     *   java.lang.Class.getResourceAsStream()과 동일하지만, classpath 루트 기준이라
     *   경로 앞에 '/'를 붙이지 않아도 된다.
     *
     * [XMLMapperBuilder]
     *   MyBatis 내부에서 Mapper XML을 파싱하는 클래스.
     *   parse() 호출 시 아래 작업을 수행한다.
     *     1. <mapper namespace="..."> 를 읽어 어떤 Mapper 인터페이스와 연결할지 파악
     *     2. <select>, <insert>, <update>, <delete> 태그를 MappedStatement 객체로 변환
     *     3. MappedStatement를 Configuration에 등록
     *        (키: "com.binghe.repository.ItemMapper.save" 형태)
     *   이후 SqlSession이 mapper.save()를 호출하면
     *   Configuration에서 해당 키로 MappedStatement를 찾아 SQL을 실행한다.
     *
     * [resource 파라미터 (세 번째 인자)]
     *   파싱된 XML의 식별자로 사용된다.
     *   Configuration 내부에서 동일한 XML을 중복 파싱하지 않도록 체크하는 데 쓰인다.
     *   보통 classpath 상의 파일 경로 문자열을 그대로 넘긴다.
     */
    private void registerMapperXml(Configuration configuration, String resource) {
        try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
            XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                    inputStream,
                    configuration,
                    resource,
                    configuration.getSqlFragments()
            );
            mapperBuilder.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(URL);
        config.setUsername(DB_USER);
        config.setPassword(DB_PASSWORD);
        return new HikariDataSource(config);
    }
}
