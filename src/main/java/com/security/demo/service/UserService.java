package com.security.demo.service;

import com.security.demo.bean.Role;
import com.security.demo.bean.UserBean;
import com.security.demo.bean.dto.RoleSmallDto;
import com.security.demo.bean.dto.UserDto;
import com.security.demo.exception.EntityNotFoundException;
import com.security.demo.mapper.RoleMapper;
import com.security.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    public int add(UserBean userBean){
        return userMapper.add(userBean);
    }

    public UserDto findByName(String userName) {
        UserDto byUserName = userMapper.findByUserName(userName);
        if (byUserName == null) {
            throw new EntityNotFoundException(UserDto.class, "name", userName);
        }

        Set<RoleSmallDto> roleById = roleMapper.findRoleById(byUserName.getId());
        byUserName.setRoles(roleById);

        return byUserName;
    }


}
