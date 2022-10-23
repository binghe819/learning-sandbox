package com.binghe.springtransactionalandaop.persistence;

import com.binghe.springtransactionalandaop.domain.Customer;
import com.binghe.springtransactionalandaop.domain.Money;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class CustomerDaoJdbc implements CustomerDao {

    private static final RowMapper<Customer> mapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        BigDecimal balance = rs.getBigDecimal("balance");
        return new Customer(id, name, Money.of(balance));
    };

    private final JdbcTemplate jdbcTemplate;

    public CustomerDaoJdbc(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Long add(Customer customer) {
        String sql = "INSERT INTO customers(name, balance) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update((con) -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, customer.getName());
            ps.setBigDecimal(2, customer.getBalance().getValue());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    @Override
    public Customer findById(Long id) {
        String sql = "SELECT * FROM customers WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, mapper, id);
    }

    @Override
    public List<Customer> findAll() {
        String sql = "SELECT * FROM customers ORDER BY id";
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public void update(Customer customer) {
        String sql = "UPDATE customers SET name = ?, balance = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                customer.getName(), customer.getBalance().getValue(), customer.getId());
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM customers";
        jdbcTemplate.update(sql);
    }
}
