package com.binghe.domain;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepositoryJdbc implements MemberRepository {
    private static final RowMapper<Member> mapper = (rs, rowNum) -> {
        Long id = rs.getLong("id");
        String name = rs.getString("name");
        String address = rs.getString("address");
        String description = rs.getString("description");
        return new Member(id, name, address, description);
    };

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long save(Member member) {
        String sql = "INSERT INTO members(name, address, description) VALUES (?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, member.getName());
            ps.setString(2, member.getAddress());
            ps.setString(3, member.getDescription());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "SELECT * FROM members WHERE id = ?";
        return Optional.ofNullable(jdbcTemplate.queryForObject(sql, mapper, id));
    }

    @Override
    public List<Member> getAll() {
        String sql = "SELECT * FROM members ORDER BY id";
        return jdbcTemplate.query(sql, mapper);
    }

    @Override
    public void deleteAll() {
        String sql = "DELETE FROM members";
        jdbcTemplate.update(sql);
    }

    @Override
    public int getCount() {
        String sql = "SELECT COUNT(id) FROM members";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);

        if (count == null) {
            return 0;
        }

        return count;
    }

    @Override
    public void update(Member member) {
        String sql = "UPDATE members SET name = ?, address = ?, description = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                member.getName(), member.getAddress(), member.getDescription(), member.getId());
    }
}
