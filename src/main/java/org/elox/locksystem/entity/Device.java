package org.elox.locksystem.entity;

import lombok.Data;

@Data
public class Device {

    private Long id;
    private String deviceName;
    private String deviceKey; // 设备密钥
}