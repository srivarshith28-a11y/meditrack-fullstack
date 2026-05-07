package com.meditrak.repository;

import com.meditrak.dto.WasteEntryForm;
import com.meditrak.model.WasteEntry;
import java.time.LocalDate;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class WasteRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<WasteEntry> wasteRowMapper = (rs, rowNum) ->
        new WasteEntry(
            rs.getLong("id"),
            rs.getLong("hospital_id"),
            rs.getString("waste_type"),
            rs.getDouble("quantity_kg"),
            rs.getString("bin_color"),
            rs.getString("collection_status"),
            rs.getDate("pickup_date").toLocalDate()
        );

    public WasteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(WasteEntryForm form) {
        jdbcTemplate.update(
            """
            insert into waste_entries(hospital_id, waste_type, quantity_kg, bin_color, collection_status, pickup_date)
            values (?, ?, ?, ?, 'Scheduled', ?)
            """,
            form.getHospitalId(),
            form.getWasteType(),
            form.getQuantityKg(),
            form.getBinColor(),
            LocalDate.parse(form.getPickupDate())
        );
    }

    public List<WasteEntry> findAll() {
        return jdbcTemplate.query("select * from waste_entries order by pickup_date desc", wasteRowMapper);
    }

    public Double totalWasteByZone(String zone) {
        Double total = jdbcTemplate.queryForObject(
            """
            select coalesce(sum(w.quantity_kg), 0)
            from waste_entries w
            join hospitals h on h.id = w.hospital_id
            where h.zone = ?
            """,
            Double.class,
            zone
        );
        return total == null ? 0.0 : total;
    }
}
