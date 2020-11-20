package com.security.demo.bean.dto;


import lombok.Data;

import java.io.Serializable;

/**
 * 登录返回值角色值
 */
@Data
public class RoleSmallDto implements Serializable {

    private Long role_id;

    private String name;

    private Integer level;
}
