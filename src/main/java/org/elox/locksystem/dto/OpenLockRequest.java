package org.elox.locksystem.dto;

import lombok.Data;

@Data
public class OpenLockRequest {
    private Long userId;
    private Long deviceId;
}