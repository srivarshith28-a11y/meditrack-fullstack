package com.meditrak.repository;

import com.meditrak.model.RoutePlan;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class RouteRepository {
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<RoutePlan> routeRowMapper = (rs, rowNum) ->
        new RoutePlan(
            rs.getLong("id"),
            rs.getString("zone"),
            rs.getString("vehicle_number"),
            rs.getString("driver_name"),
            rs.getDouble("estimated_waste_kg"),
            rs.getDouble("optimized_distance_km")
        );

    public RouteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<RoutePlan> findAll() {
        return jdbcTemplate.query("select * from routes order by optimized_distance_km asc", routeRowMapper);
    }

    public List<RoutePlan> findByZone(String zone) {
        return jdbcTemplate.query(
            "select * from routes where zone = ? order by optimized_distance_km asc",
            routeRowMapper,
            zone
        );
    }
}
