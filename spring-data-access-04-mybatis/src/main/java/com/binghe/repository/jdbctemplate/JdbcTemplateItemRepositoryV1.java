package com.binghe.repository.jdbctemplate;

import com.binghe.domain.Item;
import com.binghe.repository.ItemRepository;
import com.binghe.repository.ItemSearchCondition;
import com.binghe.repository.ItemUpdateDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcTemplateItemRepositoryV1 implements ItemRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcTemplateItemRepositoryV1.class);

    private final JdbcTemplate template;

    public JdbcTemplateItemRepositoryV1(DataSource datasource) {
        this.template = new JdbcTemplate(datasource);
    }

    @Override
    public Long save(Item item) {
        String sql = "insert into item (item_name, price, quantity) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            // 자동 증가 키
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, item.getItemName());
            ps.setInt(2, item.getPrice());
            ps.setInt(3, item.getQuantity());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        String sql = "update item set item_name=?, price=?, quantity=? where id=?";
        template.update(sql,
                updateParam.getItemName(),
                updateParam.getPrice(),
                updateParam.getQuantity(),
                itemId);
    }

    @Override
    public Optional<Item> findById(Long id) {
        String sql = "select id, item_name, price, quantity from item where id = ?";
        try {
            // queryForObject 는 하나만 조회할때 사용
            Item item = template.queryForObject(sql, itemRowMapper(), id);
            return Optional.of(item);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    /**
     * [MyBatis 없이 동적 쿼리를 처리하는 방식의 문제점]
     *
     * MyBatis 없이 동적 쿼리를 작성하면 아래와 같이 조건 개수만큼 if 분기가 늘어나고,
     * WHERE / AND 키워드를 직접 붙여야 하기 때문에 코드가 복잡해진다.
     *
     * 문제 1 - WHERE/AND 순서 관리를 직접 해야 한다:
     *   조건이 처음 추가될 때는 "WHERE"를, 두 번째부터는 "AND"를 붙여야 한다.
     *   이를 andFlag 같은 boolean 변수로 직접 추적해야 하므로 실수가 생기기 쉽다.
     *
     * 문제 2 - 파라미터 바인딩 순서를 수동으로 맞춰야 한다:
     *   PreparedStatement의 ?와 param 리스트의 순서가 반드시 일치해야 하는데,
     *   조건이 많아질수록 순서를 놓치기 쉽고 디버깅도 어렵다.
     *
     * 문제 3 - 조건이 늘어날수록 복잡도가 급격히 증가한다:
     *   조건이 N개라면 경우의 수가 최대 2^N가지가 되어, 모든 케이스를 커버하는
     *   if 분기를 직접 작성하고 유지보수하기 매우 어렵다.
     *
     * → MyBatis의 <if>, <where>, <foreach> 같은 동적 SQL 태그를 사용하면
     *   이 모든 문제를 XML/어노테이션 레벨에서 선언적으로 해결할 수 있다.
     */
    @Override
    public List<Item> findAll(ItemSearchCondition cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();

        String sql = "select id, item_name, price, quantity from item";

        // 동적쿼리: 조건이 하나라도 있으면 WHERE 절을 붙인다.
        if (StringUtils.hasText(itemName) || maxPrice != null) {
            sql += " where";
        }
        // andFlag: 첫 번째 조건 이후부터 AND를 붙이기 위한 플래그
        boolean andFlag = false;
        List<Object> param = new ArrayList<>();
        if (StringUtils.hasText(itemName)) {
            sql += " item_name like concat('%',?,'%')";
            param.add(itemName);
            andFlag = true;
        }
        if (maxPrice != null) {
            if (andFlag) {
                sql += " and";
            }
            sql += " price <= ?";
            param.add(maxPrice);
        }
        log.info("sql={}", sql);
        return template.query(sql, itemRowMapper(), param.toArray());
    }

    private RowMapper<Item> itemRowMapper() {
        return (rs, rowNum) -> {
            Item item = new Item();
            item.setId(rs.getLong("id"));
            item.setItemName((rs.getString("item_name")));
            item.setPrice(rs.getInt("price"));
            item.setQuantity(rs.getInt("quantity"));
            return item;
        };
    }
}
