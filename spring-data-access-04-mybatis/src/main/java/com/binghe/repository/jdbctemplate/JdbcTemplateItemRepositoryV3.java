package com.binghe.repository.jdbctemplate;

import com.binghe.domain.Item;
import com.binghe.repository.ItemRepository;
import com.binghe.repository.ItemSearchCondition;
import com.binghe.repository.ItemUpdateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * V2 대비 개선 사항: SimpleJdbcInsert — INSERT SQL을 직접 작성하지 않아도 되는 방식
 *
 * ────────────────────────────────────────────────────────────────────────────
 * [SimpleJdbcInsert란?]
 * ────────────────────────────────────────────────────────────────────────────
 * Spring이 제공하는 INSERT 전용 헬퍼 클래스다.
 * 초기화 시점에 DataSource를 통해 DB 메타데이터(테이블 컬럼 정보)를 읽어,
 * INSERT SQL을 런타임에 자동으로 생성한다.
 *
 * V1/V2에서는 INSERT SQL을 직접 문자열로 작성해야 했다.
 *   V1: "insert into item (item_name, price, quantity) values (?, ?, ?)"
 *   V2: "insert into item (item_name, price, quantity) values (:itemName, :price, :quantity)"
 *
 * V3에서는 테이블명과 PK 컬럼명만 설정하면 SQL 없이 INSERT가 가능하다.
 *   → 컬럼이 추가/변경되어도 SQL 문자열을 수정할 필요가 없다.
 *
 * ────────────────────────────────────────────────────────────────────────────
 * [생성자에서의 SimpleJdbcInsert 설정]
 * ────────────────────────────────────────────────────────────────────────────
 * .withTableName("item")
 *   - INSERT 대상 테이블을 지정한다.
 *   - 내부적으로 이 테이블의 컬럼 메타데이터를 DB에서 조회해 SQL을 자동 생성한다.
 *
 * .usingGeneratedKeyColumns("id")
 *   - AUTO_INCREMENT로 DB가 생성하는 PK 컬럼명을 지정한다.
 *   - executeAndReturnKey() 호출 시 INSERT 후 생성된 키를 반환받을 수 있게 된다.
 *   - V1/V2에서는 KeyHolder + GeneratedKeyHolder를 직접 생성하고
 *     template.update(connection -> { ... }, keyHolder) 형태로 처리해야 했지만,
 *     V3에서는 executeAndReturnKey() 한 줄로 동일한 결과를 얻는다.
 *
 * .usingColumns("item_name", "price", "quantity")  // 생략 가능
 *   - INSERT에 포함할 컬럼을 명시적으로 지정하는 옵션이다.
 *   - 생략하면 usingGeneratedKeyColumns에 지정된 PK 컬럼을 제외한
 *     나머지 컬럼 전체를 자동으로 INSERT 대상으로 포함한다.
 *   - 특정 컬럼만 INSERT하고 싶을 때(예: created_at 같은 default 컬럼 제외) 사용한다.
 */
public class JdbcTemplateItemRepositoryV3 implements ItemRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcTemplateItemRepositoryV3.class);

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcTemplateItemRepositoryV3(DataSource datasource) {
        this.template = new NamedParameterJdbcTemplate(datasource);
        this.jdbcInsert = new SimpleJdbcInsert(datasource)
                .withTableName("item")
                .usingGeneratedKeyColumns("id");
//                .usingColumns("item_name", "price", "quantity") // 생략가능
    }

    @Override
    public Long save(Item item) {
        // BeanPropertySqlParameterSource로 Item의 필드를 테이블 컬럼에 자동 매핑한다.
        // SimpleJdbcInsert가 메타데이터를 기반으로 INSERT SQL을 생성하고 실행한 뒤,
        // AUTO_INCREMENT로 생성된 id 값을 Number 타입으로 반환한다.
        // V1/V2처럼 KeyHolder나 INSERT SQL 문자열을 직접 다룰 필요가 없다.
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);
        Number key = jdbcInsert.executeAndReturnKey(param);
        return key.longValue();
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=:itemName, price=:price, quantity=:quantity where id=:id";

        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("itemName", updateParam.getItemName())
                .addValue("price", updateParam.getPrice())
                .addValue("quantity", updateParam.getQuantity())
                .addValue("id", itemId);
        template.update(sql, param);
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id = :id";
        try {
            Map<String, Long> param = Map.of("id", id);
            // queryForObject 는 하나만 조회할때 사용
            Item item = template.queryForObject(sql, param, itemRowMapper());
            return Optional.of(item);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Item> findAll(ItemSearchCondition cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);

        String sql = "select id, item_name, price, quantity from item";

        // 동적쿼리
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }
        boolean andFlag = false;

        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%',:itemName,'%')";
            andFlag = true;
        }
        if (maxPrice != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " price <= :maxPrice";
        }
        log.info("sql={}", sql);
        return template.query(sql, param, itemRowMapper());

    }

    private RowMapper<Item> itemRowMapper() {
        return BeanPropertyRowMapper.newInstance(Item.class); // camel 변환 지원
    }
}
