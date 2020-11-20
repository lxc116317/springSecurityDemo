package com.security.demo.service;

import com.security.demo.bean.Menu;
import com.security.demo.bean.Role;
import com.security.demo.bean.dto.UserDto;
import com.security.demo.mapper.RoleMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleMapper roleMapper;


    public List<GrantedAuthority> mapToGrantedAuthorities(UserDto user){
        Set<String> permissions = new HashSet<>();
        // 如果是管理员直接返回
        /**
         * 默认是false，如何变成true
         */
        if ( user.getUsername().equals("admin")){
            user.setIsAdmin(true);
        }
        if (user.getIsAdmin()) {
            permissions.add("admin");
            return permissions.stream().map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        Set<Role> roles = roleMapper.findByUserId(user.getId());

        roles.forEach(e->{
            Set<Menu> menuById = roleMapper.findMenuById(e.getRoleId());
            e.setMenus(menuById);
        });

        permissions = roles.stream().flatMap(role -> role.getMenus().stream()).filter(menu -> StringUtils
                .isNotBlank(menu.getPermission())).map(Menu::getPermission).collect(Collectors.toSet());

        return permissions.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}