package org.elox.locksystem.dao;

import org.elox.locksystem.entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DeviceDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Device findById(Long id) {
        String sql = "SELECT id, device_name, device_key FROM device WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Device.class), id);
    }
}