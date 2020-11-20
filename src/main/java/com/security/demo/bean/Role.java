package com.security.demo.bean;

import lombok.Data;

import java.util.Set;

@Data
public class Role {
    private Long roleId;
    private String name;
    private String level;

    private Set<Menu> menus;
}
