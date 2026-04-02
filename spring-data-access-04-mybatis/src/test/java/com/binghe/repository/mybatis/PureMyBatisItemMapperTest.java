package com.binghe.repository.mybatis;

import com.binghe.domain.Item;
import com.binghe.repository.ItemSearchCondition;
import com.binghe.repository.ItemUpdateDto;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.type.TypeAliasRegistry;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 순수 MyBatis로 ItemMapper를 사용하는 테스트.
 * Spring 없이 MyBatis의 내부 구성 요소를 직접 조립한다.
 *
 * [Spring-MyBatis에서 감춰진 것들을 직접 드러낸다]
 *
 *  1. SqlSessionFactoryBuilder  - Configuration을 파싱해 SqlSessionFactory를 한 번만 만든다.
 *  2. Configuration             - MyBatis 전역 설정 객체. XML의 <configuration>에 대응한다.
 *  3. Environment               - 어떤 DataSource + TransactionFactory를 쓸지 묶은 단위.
 *  4. SqlSessionFactory         - SqlSession을 찍어내는 팩토리. 앱 생명주기 동안 하나만 유지.
 *  5. SqlSession                - 실제 DB 커넥션을 감싼 객체. 트랜잭션 단위로 열고 닫는다.
 *                                 openSession(false) → autoCommit OFF → commit()/rollback() 필수.
 *                                 openSession(true)  → autoCommit ON  → 각 쿼리가 즉시 커밋.
 *  6. session.getMapper()       - ItemMapper 인터페이스의 JDK 동적 프록시를 반환한다.
 *                                 이 프록시가 메서드 호출을 SqlSession으로 위임한다.
 */
class PureMyBatisItemMapperTest {

    private static final Logger log = LoggerFactory.getLogger(PureMyBatisItemMapperTest.class);

    /**
     * SqlSessionFactory는 생성 비용이 크다(XML 파싱, 설정 로딩).
     * 앱에서 한 번만 만들어 싱글톤으로 재사용하는 것이 정석이다.
     * 테스트에서도 @BeforeAll로 클래스 전체에 걸쳐 한 번만 생성한다.
     */
    private static SqlSessionFactory sqlSessionFactory;

    /**
     * 각 테스트는 별도의 SqlSession을 가진다.
     * SqlSession은 스레드 안전하지 않으므로, 요청/트랜잭션 단위로 열고 닫아야 한다.
     */
    private SqlSession session;

    // ─────────────────────────────────────────────────────────────────
    // 1단계: SqlSessionFactory 조립 (앱 기동 시 한 번)
    // ─────────────────────────────────────────────────────────────────

    @BeforeAll
    static void buildSqlSessionFactory() {
        // ── DataSource ──────────────────────────────────────────────
        // Spring-MyBatis에서는 DataSourceConfig @Bean이 이걸 대신했다.
        //
        // [Spring-MyBatis와의 대응표]
        //   new HikariDataSource(config)              ← DataSourceConfig @Bean
        //   new JdbcTransactionFactory()              ← SpringManagedTransactionFactory (자동)
        //   configuration.addMapper(ItemMapper.class) ← SqlSessionFactoryBean.setMapperLocations()
        //   aliasRegistry.registerAlias("Item", ...)  ← SqlSessionFactoryBean.setTypeAliasesPackage()
        //   new SqlSessionFactoryBuilder().build(cfg) ← SqlSessionFactoryBean.getObject() 내부에서 실행
        //
        // [조립 흐름]
        //   DataSource (HikariCP)
        //       └─ Environment (DataSource + JdbcTransactionFactory 묶음)
        //               └─ Configuration (전역 설정: alias, underscoreToCamelCase, mapper 등록)
        //                       └─ SqlSessionFactoryBuilder.build(configuration)
        //                               → SqlSessionFactory (싱글톤으로 유지)
        DataSource dataSource = createDataSource();

        // ── Environment ─────────────────────────────────────────────
        // MyBatis가 사용할 DataSource와 TransactionFactory를 묶는 단위.
        // JdbcTransactionFactory: JDBC Connection의 autoCommit 플래그를 직접 제어한다.
        // (Spring 환경에서는 SpringManagedTransactionFactory가 사용된다)
        Environment environment = new Environment(
                "development",               // 환경 식별자 (mybatis-config.xml의 <environment id="...">에 대응)
                new JdbcTransactionFactory(), // 트랜잭션 관리 방식
                dataSource
        );

        // ── Configuration ────────────────────────────────────────────
        // MyBatis의 모든 설정을 담는 중앙 객체.
        // XML 기반이면 mybatis-config.xml을 파싱해 만들지만,
        // 여기서는 Java 코드로 직접 조립한다.
        Configuration configuration = new Configuration(environment);

        // snake_case 컬럼(item_name)을 camelCase 필드(itemName)로 자동 변환
        configuration.setMapUnderscoreToCamelCase(true);

        // TypeAlias: XML resultType에서 "com.binghe.domain.Item" 대신 "Item"으로 쓸 수 있게 한다.
        // Spring-MyBatis에서는 SqlSessionFactoryBean.setTypeAliasesPackage()가 이걸 대신했다.
        TypeAliasRegistry aliasRegistry = configuration.getTypeAliasRegistry();
        aliasRegistry.registerAlias("Item", Item.class);

        // Mapper XML 등록: namespace + 각 SQL 구문을 Configuration에 적재한다.
        // Spring-MyBatis에서는 SqlSessionFactoryBean.setMapperLocations()가 이걸 대신했다.
        //
        // [주의] configuration.addMapper(ItemMapper.class)를 쓰면 안 되는 이유:
        //   addMapper()는 MyBatis 컨벤션에 따라 XML을 슬래시 경로로 찾는다.
        //     → 탐색 경로: com/binghe/repository/mybatis/ItemMapper.xml
        //   그런데 이 프로젝트의 XML 실제 위치는 점(dot) 디렉토리다.
        //     → 실제 경로: com.binghe.repository.mybatis/itemMapper.xml
        //   경로가 다르므로 XML을 찾지 못하고 "Invalid bound statement" 에러가 발생한다.
        //   Spring-MyBatis는 setMapperLocations()로 경로를 직접 지정하므로 이 문제가 없었다.
        //
        //   → 해결: XMLMapperBuilder로 XML을 직접 로드한다.
        //     parse() 내부에서 bindMapperForNamespace()가 호출되어
        //     namespace(=ItemMapper FQCN)를 기반으로 인터페이스도 함께 등록된다.
        String xmlPath = "com.binghe.repository.mybatis/itemMapper.xml";
        InputStream xmlStream = PureMyBatisItemMapperTest.class.getClassLoader().getResourceAsStream(xmlPath);
        XMLMapperBuilder mapperBuilder = new XMLMapperBuilder(
                xmlStream, configuration, xmlPath, configuration.getSqlFragments());
        mapperBuilder.parse(); // SQL 구문 적재 + ItemMapper 인터페이스 등록까지 수행

        // ── SqlSessionFactoryBuilder ──────────────────────────────────
        // Configuration을 받아 SqlSessionFactory를 딱 한 번 만든다.
        // 이 빌더 자체는 이후에 버려도 된다(일회용).
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

        log.info("[1단계 완료] SqlSessionFactory 생성: {}", sqlSessionFactory.getClass().getSimpleName());
    }

    private static DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/spring-data-test");
        config.setUsername("root");
        config.setPassword("1234");
        config.setMaximumPoolSize(5);
        return new HikariDataSource(config);
    }

    // ─────────────────────────────────────────────────────────────────
    // 2단계: 테스트마다 SqlSession 열기 / 닫기
    // ─────────────────────────────────────────────────────────────────

    @BeforeEach
    void openSession() {
        // openSession(false) → autoCommit=OFF
        // 이후 commit() 또는 rollback()을 명시적으로 호출해야 한다.
        // Spring의 @Transactional이 대신 해주던 일을 여기서 직접 한다.
        //
        // openSession(true)  → autoCommit=ON → 각 쿼리가 즉시 커밋되어 명시적 commit() 불필요.
        // openSession(false) → autoCommit=OFF → commit()/rollback() 명시 필요. 트랜잭션 제어 가능.
        session = sqlSessionFactory.openSession(false);
        log.info("[2단계] SqlSession 오픈: {}", session.getClass().getSimpleName());
    }

    @AfterEach
    void rollbackAndCloseSession() {
        // [테스트 격리] Spring의 @Transactional 테스트 자동 롤백과 동일한 효과를 직접 구현한다.
        //   Spring 환경: @Transactional 붙은 테스트는 끝날 때 자동으로 rollback() 호출
        //   순수 MyBatis: Spring이 없으므로 여기서 직접 rollback() → close() 호출
        session.rollback();
        session.close();
        log.info("[AfterEach] SqlSession 롤백 & 클로즈");
    }

    // ─────────────────────────────────────────────────────────────────
    // 3단계: 실제 테스트 - session.getMapper()로 프록시를 꺼내 사용
    // ─────────────────────────────────────────────────────────────────

    @Test
    void getMapper_반환객체는_JDK_동적_프록시() {
        // session.getMapper()는 ItemMapper 인터페이스의 구현체 클래스를 찾는 게 아니라
        // JDK 동적 프록시(Proxy$N)를 런타임에 생성해 반환한다.
        ItemMapper itemMapper = session.getMapper(ItemMapper.class);

        log.info("itemMapper 실제 타입: {}", itemMapper.getClass().getName());
        assertThat(itemMapper.getClass().getName()).contains("$Proxy");
    }

    @Test
    void save_autoCommitOFF_커밋전에는_다른_세션에서_보이지_않는다() {
        // [핵심 테스트 1: 트랜잭션 격리 직접 확인]
        //   같은 세션 : INSERT 후 자신이 쓴 데이터는 보인다.
        //   다른 세션 : 커밋 전이므로 안 보인다.
        //
        // Spring @Transactional 테스트에서는 이 격리가 자동으로 보장되지만,
        // 여기서는 두 세션을 직접 열어 커밋 전/후의 가시성 차이를 눈으로 확인한다.
        ItemMapper itemMapper = session.getMapper(ItemMapper.class);

        Item item = new Item(null, "itemA", 1000, 10);
        itemMapper.save(item); // INSERT 실행, 아직 커밋 안됨

        // 같은 session 안에서는 조회 가능 (자신이 변경한 내용은 자신의 세션에서 보인다)
        Optional<Item> foundInSameSession = itemMapper.findById(item.getId());
        assertThat(foundInSameSession).isPresent();
        log.info("INSERT 후 item.getId()={} (keyProperty로 주입된 값)", item.getId());

        // 별도 세션(autoCommit=true)으로 조회 → 아직 커밋 전이므로 보이지 않아야 한다.
        // MySQL 기본 격리 수준(REPEATABLE READ)은 다른 트랜잭션의 미커밋 데이터를 읽지 않는다.
        try (SqlSession otherSession = sqlSessionFactory.openSession(true)) {
            ItemMapper otherMapper = otherSession.getMapper(ItemMapper.class);
            Optional<Item> notYetVisible = otherMapper.findById(item.getId());
            assertThat(notYetVisible).isEmpty();
            log.info("커밋 전: 다른 세션에서 조회 결과 없음 (트랜잭션 격리)");
        }

        // AfterEach에서 session.rollback()이 실행되어 테스트 데이터가 자동 정리된다.
    }

    @Test
    void save_useGeneratedKeys_id가_파라미터_객체에_주입된다() {
        ItemMapper itemMapper = session.getMapper(ItemMapper.class);
        Item item = new Item(null, "itemA", 1000, 10);

        assertThat(item.getId()).isNull(); // INSERT 전: id 없음

        itemMapper.save(item);

        // <insert useGeneratedKeys="true" keyProperty="id"> 설정으로
        // JDBC getGeneratedKeys()가 반환한 PK가 item.id에 자동 주입된다.
        assertThat(item.getId()).isNotNull();
        log.info("INSERT 후 item.getId()={}", item.getId());
    }

    @Test
    void update() {
        ItemMapper itemMapper = session.getMapper(ItemMapper.class);

        Item item = new Item(null, "itemA", 1000, 10);
        itemMapper.save(item);

        itemMapper.update(item.getId(), new ItemUpdateDto("itemB", 2000, 20));

        Item updated = itemMapper.findById(item.getId()).orElseThrow();
        assertThat(updated.getItemName()).isEqualTo("itemB");
        assertThat(updated.getPrice()).isEqualTo(2000);
        assertThat(updated.getQuantity()).isEqualTo(20);
    }

    @Test
    void findById_없는_id는_빈_Optional() {
        ItemMapper itemMapper = session.getMapper(ItemMapper.class);

        Optional<Item> result = itemMapper.findById(Long.MAX_VALUE);

        assertThat(result).isEmpty();
    }

    @Test
    void findAll_동적쿼리_조건없음() {
        ItemMapper itemMapper = session.getMapper(ItemMapper.class);
        itemMapper.save(new Item(null, "itemA", 1000, 10));
        itemMapper.save(new Item(null, "itemB", 2000, 20));

        // ItemSearchCondition이 null이면 XML의 <where><if> 조건이 모두 생략 → 전체 조회
        List<Item> items = itemMapper.findAll(new ItemSearchCondition(null, null));

        assertThat(items).hasSizeGreaterThanOrEqualTo(2);
        assertThat(items).extracting(Item::getItemName).contains("itemA", "itemB");
    }

    @Test
    void findAll_동적쿼리_itemName_조건() {
        ItemMapper itemMapper = session.getMapper(ItemMapper.class);
        itemMapper.save(new Item(null, "itemA", 1000, 10));
        itemMapper.save(new Item(null, "itemB", 2000, 20));
        itemMapper.save(new Item(null, "special", 500, 5));

        // XML: <if test="itemName != null and itemName != ''"> AND item_name LIKE ... </if>
        List<Item> items = itemMapper.findAll(new ItemSearchCondition("item", null));

        assertThat(items).extracting(Item::getItemName)
                .contains("itemA", "itemB")
                .doesNotContain("special");
    }

    @Test
    void rollback_명시적_롤백으로_변경사항이_취소된다() {
        // [핵심 테스트 2: rollback() 동작 직접 확인]
        //   session.rollback() 호출 후 다른 세션에서 조회하면 없어짐.
        //   Spring @Transactional이 테스트 종료 시 내부적으로 하는 일이 바로 이것이다.
        ItemMapper itemMapper = session.getMapper(ItemMapper.class);

        Item item = new Item(null, "willBeRolledBack", 9999, 1);
        itemMapper.save(item);
        Long savedId = item.getId();
        assertThat(itemMapper.findById(savedId)).isPresent();

        // 명시적 롤백: INSERT가 취소된다.
        session.rollback();

        // 롤백 후 다른 세션에서 조회 → 존재하지 않아야 한다.
        try (SqlSession otherSession = sqlSessionFactory.openSession(true)) {
            Optional<Item> result = otherSession.getMapper(ItemMapper.class).findById(savedId);
            assertThat(result).isEmpty();
            log.info("롤백 후: id={} 조회 결과 없음", savedId);
        }
    }
}
