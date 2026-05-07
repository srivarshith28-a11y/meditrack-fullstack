package com.meditrak.repository;

import com.meditrak.dto.HospitalForm;
import com.meditrak.model.Hospital;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class HospitalRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Hospital> hospitalRowMapper = (rs, rowNum) ->
        new Hospital(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("zone"),
            rs.getString("contact_person"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("address")
        );

    public HospitalRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Hospital> findAll() {
        return jdbcTemplate.query("select * from hospitals order by name", hospitalRowMapper);
    }

    public List<Hospital> findByZone(String zone) {
        return jdbcTemplate.query("select * from hospitals where zone = ? order by name", hospitalRowMapper, zone);
    }

    public void save(HospitalForm form) {
        jdbcTemplate.update(
            "insert into hospitals(name, zone, contact_person, email, phone, address) values (?, ?, ?, ?, ?, ?)",
            form.getName(),
            form.getZone(),
            form.getContactPerson(),
            form.getEmail(),
            form.getPhone(),
            form.getAddress()
        );
    }
}
