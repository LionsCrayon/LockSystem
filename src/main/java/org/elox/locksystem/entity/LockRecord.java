package org.elox.locksystem.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LockRecord {
    private String requestId; // 业务请求ID
    private Long userId;
    private Long deviceId;
    private LocalDateTime requestTime;
    private LocalDateTime completeTime;
    private String status; // PENDING, SUCCESS, FAILED
    private String result; // 成功/失败详细信息
}