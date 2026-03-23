package org.elox.locksystem.entity;

import lombok.Data;


@Data
public class User {

    private Long id;
    private String name;
    private String phone;
    // 权限等字段省略
}
