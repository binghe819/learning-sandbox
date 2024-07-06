package com.binghe;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MemberDao {

    private static final RowMapper<Member> memberRowMapper = new RowMapper<Member>() {
        @Override
        public Member mapRow(ResultSet resultSet, int i) throws SQLException {
            Member member = new Member();
            member.setId(resultSet.getLong("id"));
            member.setName(resultSet.getString("name"));
            member.setAge(resultSet.getInt("age"));
            member.setDescription(resultSet.getString("description"));
            return member;
        }
    };

    private final JdbcTemplate jdbcTemplate;

    public MemberDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Optional<Member> findById(Long id) {
        return Optional
                .ofNullable(this.jdbcTemplate.queryForObject("select * from members where id = ?", new Object[]{id}, memberRowMapper));
    }
}
