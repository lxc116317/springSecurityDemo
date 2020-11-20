package com.security.demo.mapper;

import com.security.demo.bean.UserBean;
import com.security.demo.bean.dto.UserDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;


public interface UserMapper {

    /**
     * 新增用户
     * @param userBean
     * @return
     */
    @Insert(" insert into sys_user (username,password,enabled) values (#{userName},#{password},1) ")
    int add(UserBean userBean);

    @Select(" select * from sys_user where username = #{userName}")
    UserDto findByUserName(String userName);

}
