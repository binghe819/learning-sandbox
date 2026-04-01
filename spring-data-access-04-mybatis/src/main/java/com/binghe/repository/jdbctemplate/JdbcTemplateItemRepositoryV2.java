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
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * V1 대비 개선 사항: NamedParameterJdbcTemplate + 다양한 파라미터 바인딩 방식 + BeanPropertyRowMapper
 *
 * ────────────────────────────────────────────────────────────────────────────
 * [1] NamedParameterJdbcTemplate — ? 대신 이름 기반 파라미터(:name)
 * ────────────────────────────────────────────────────────────────────────────
 * V1의 JdbcTemplate은 파라미터를 ?로 표기하고 순서대로 값을 전달한다.
 *   SQL:    "insert into item values (?, ?, ?)"
 *   바인딩:  ps.setString(1, ...), ps.setInt(2, ...), ps.setInt(3, ...)
 *
 * 파라미터가 많거나 수정이 발생하면 SQL의 ? 위치와 setXxx() 인덱스를 함께
 * 바꿔야 하므로 실수가 생기기 쉽다.
 *
 * NamedParameterJdbcTemplate은 파라미터를 :itemName, :price 처럼 이름으로 지정하므로
 * 순서에 의존하지 않아 가독성과 유지보수성이 높아진다.
 *   SQL:    "insert into item values (:itemName, :price, :quantity)"
 *   바인딩:  파라미터 소스에 이름-값 쌍으로 전달
 *
 * ────────────────────────────────────────────────────────────────────────────
 * [2] 파라미터 바인딩 방식 3가지
 * ────────────────────────────────────────────────────────────────────────────
 * (a) BeanPropertySqlParameterSource
 *     - 자바 빈(getter)의 필드명을 :파라미터명에 자동으로 매핑한다.
 *     - SQL의 :itemName → item.getItemName() 이 자동으로 연결된다.
 *     - 객체를 그대로 넘길 수 있어 코드가 매우 간결하다.
 *     - 단, SQL의 파라미터명과 객체의 필드명이 정확히 일치해야 한다.
 *     - 사용 예: save(Item item) → new BeanPropertySqlParameterSource(item)
 *
 * (b) MapSqlParameterSource
 *     - Map처럼 키-값 쌍을 직접 지정하는 방식이다.
 *     - 객체 필드명과 SQL 파라미터명이 다를 때 유연하게 매핑할 수 있다.
 *     - addValue()를 체이닝하여 여러 파라미터를 한 번에 구성할 수 있다.
 *     - 사용 예: update() → itemId처럼 객체에 없는 파라미터를 추가할 때 적합하다.
 *
 * (c) Map<String, Object>
 *     - 가장 단순한 방식으로, Map.of("id", id) 처럼 바로 생성해 전달한다.
 *     - 파라미터가 1~2개처럼 적을 때 간단하게 쓰기 좋다.
 *     - SqlParameterSource 인터페이스를 구현하지 않으므로
 *       template.query(sql, map, rowMapper) 형태로 Map을 직접 전달한다.
 *     - 사용 예: findById() → Map.of("id", id)
 *
 * ────────────────────────────────────────────────────────────────────────────
 * [3] BeanPropertyRowMapper — ResultSet → 객체 자동 매핑
 * ────────────────────────────────────────────────────────────────────────────
 * V1에서는 RowMapper를 직접 구현해 컬럼명과 setter를 일일이 연결했다.
 *   item.setId(rs.getLong("id"));
 *   item.setItemName(rs.getString("item_name"));  // snake_case → camelCase 수동 변환
 *
 * BeanPropertyRowMapper는 ResultSet의 컬럼명을 자바 빈의 필드명에 자동으로 매핑한다.
 * 이때 snake_case(item_name) → camelCase(itemName) 변환도 자동으로 처리하므로
 * 별도의 RowMapper 구현 없이 BeanPropertyRowMapper.newInstance(Item.class) 한 줄로 끝난다.
 * 단, 매핑이 되려면 Item 클래스에 기본 생성자와 setter가 있어야 한다.
 */
public class JdbcTemplateItemRepositoryV2 implements ItemRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcTemplateItemRepositoryV2.class);

//    private final JdbcTemplate template;

    private final NamedParameterJdbcTemplate template;

    public JdbcTemplateItemRepositoryV2(DataSource datasource) {
        this.template = new NamedParameterJdbcTemplate(datasource);
    }

    @Override
    public Long save(Item item) {
        String sql = "insert into item (item_name, price, quantity) values (:itemName, :price, :quantity)";

        // BeanPropertySqlParameterSource: Item의 getter(getItemName 등)를 읽어
        // :itemName, :price, :quantity에 자동으로 바인딩한다.
        SqlParameterSource param = new BeanPropertySqlParameterSource(item);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(sql, param, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=:itemName, price=:price, quantity=:quantity where id=:id";

        // MapSqlParameterSource: updateParam 객체에는 id 필드가 없으므로
        // BeanPropertySqlParameterSource를 쓸 수 없다.
        // addValue()로 :id 파라미터를 직접 추가해 유연하게 구성한다.
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
            // Map<String, Object>: 파라미터가 id 하나뿐이므로 Map.of()로 간단하게 전달한다.
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

        // BeanPropertySqlParameterSource: ItemSearchCondition의 getItemName(), getMaxPrice()를
        // :itemName, :maxPrice에 자동으로 매핑한다.
        // V1과 달리 파라미터 리스트(List<Object>)를 수동으로 관리할 필요가 없다.
        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);

        String sql = "select id, item_name, price, quantity from item";

        // 동적쿼리 (V1과 동일한 한계: WHERE/AND를 여전히 수동으로 관리해야 한다.)
        // V2에서 개선된 점은 파라미터 바인딩 방식뿐이며,
        // 동적 쿼리 구성 자체의 복잡도는 MyBatis를 사용해야 해결된다.
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
        // BeanPropertyRowMapper: ResultSet 컬럼명(item_name)을 camelCase(itemName)로 자동 변환하여
        // Item 객체의 setter에 매핑한다. V1처럼 직접 RowMapper를 구현할 필요가 없다.
        return BeanPropertyRowMapper.newInstance(Item.class); // camel 변환 지원
    }
}
