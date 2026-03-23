package org.elox.locksystem.dao;


import lombok.extern.slf4j.Slf4j;
import org.elox.locksystem.entity.LockRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
@Repository
public class LockRecordDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(LockRecord record) {
        String sql = "INSERT INTO lock_record (request_id, user_id, device_id, request_time, status) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, record.getRequestId());
            ps.setLong(2, record.getUserId());
            ps.setLong(3, record.getDeviceId());
            ps.setTimestamp(4, Timestamp.valueOf(record.getRequestTime()));
            ps.setString(5, record.getStatus());
            return ps;
        });
    }

    public void updateResult(String requestId, boolean success, String message, LocalDateTime completeTime) {
        try {
            String sql = "UPDATE lock_record SET status = ?, result = ?, complete_time = ? WHERE request_id = ?";
            int updated = jdbcTemplate.update(sql,
                    success ? "SUCCESS" : "FAILED",
                    message,
                    Timestamp.valueOf(LocalDateTime.now()),
                    requestId);
            if (updated > 0) {
                log.info("Successfully updated lock record for requestId={} to status={}", requestId, success ? "SUCCESS" : "FAILED");
            } else {
                log.warn("No record updated for requestId={}. Maybe requestId not found?", requestId);
            }
        } catch (Exception e) {
            log.error("Failed to update lock record for requestId: " + requestId, e);
            throw e; // 抛出异常让 RocketMQ 重试（但注意如果抛出异常，消息状态会变为 CONSUMED？实际上抛出异常会导致消费失败，消息会重试）
        }
    }

    public LockRecord findById(String requestId) {
        String sql = "SELECT request_id, user_id, device_id, request_time, complete_time, status, result FROM lock_record WHERE request_id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(LockRecord.class), requestId);
    }
}