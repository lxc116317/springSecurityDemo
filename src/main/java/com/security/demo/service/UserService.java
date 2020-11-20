package com.security.demo.service;

import com.security.demo.bean.UserBean;
import com.security.demo.bean.dto.RoleSmallDto;
import com.security.demo.bean.dto.UserDto;
import com.security.demo.exception.EntityNotFoundException;
import com.security.demo.mapper.RoleMapper;
import com.security.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service
@CacheConfig(cacheNames = "user")
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;


    /**
     * @Transactional 一般用于增删改，不用于查
     * @Transactional 注解只被应用到 public 方法上，如果你在 protected、private 或者默认可见性的方法上使用 @Transactional 注解，这将被忽略，也不会抛出任何异常
     * @param userBean
     * @return
     */
    @Transactional(rollbackFor = Exception.class,timeout = 5)
    public int add(UserBean userBean){
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        return userMapper.add(userBean);
    }


//    @Cacheable(key = "'id:' + #p0")
    @Cacheable(key = "'id:' + #p0")
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
