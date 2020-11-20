package com.security.demo.mapper;

import com.security.demo.bean.Menu;
import com.security.demo.bean.Role;
import com.security.demo.bean.dto.RoleSmallDto;
import org.apache.ibatis.annotations.Select;

import java.util.Set;

public interface RoleMapper {


    @Select(" SELECT r.role_id roleId,name,level FROM sys_role r left join sys_users_roles u on r.role_id = u.role_id where u.user_id = #{id} ")
    Set<Role> findByUserId(Long id);

    @Select(" select * from sys_role where role_id = #{id}")
    Set<RoleSmallDto> findRoleById(Long id);

    @Select(" select m.menu_id menuId,m.pid,m.title,m.permission from sys_menu m LEFT JOIN sys_role_menus rm on m.menu_id=rm.menu_id WHERE rm.role_id=#{id}")
    Set<Menu> findMenuById(Long id);
}