package com.security.demo.bean.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * 暂无部门
 */
@Data
public class UserDto implements Serializable {

    private Long id;

    private Set<RoleSmallDto> roles;

    private String username;

    private Boolean enabled;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private Boolean isAdmin = false;
}
