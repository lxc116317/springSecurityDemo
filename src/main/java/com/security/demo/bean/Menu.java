package com.security.demo.bean;

import lombok.Data;

@Data
public class Menu {
    private Long menuId;
    private Long pid;
    private String title;
    private String permission;

}
