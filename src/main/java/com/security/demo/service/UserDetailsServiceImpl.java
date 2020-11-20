package com.security.demo.service;

import com.security.demo.bean.dto.JwtUserDto;
import com.security.demo.bean.dto.UserDto;
import com.security.demo.exception.BadRequestException;
import com.security.demo.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        List<GrantedAuthority> authorities = Arrays.asList(
//                new SimpleGrantedAuthority("user:add"),
//                new SimpleGrantedAuthority("user:view"),
//                new SimpleGrantedAuthority("user:update"));
//
//        UserOfDetails user = new UserOfDetails(1L, username, passwordEncoder.encode("123456"), true, authorities);
//
//        if ( user == null ){
//            throw new UsernameNotFoundException("用户名或密码错误");
//        }
//
//        return user;
//    }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

            JwtUserDto jwtUserDto = null;

            UserDto user;

            try {
                user = userService.findByName(username);
            } catch (EntityNotFoundException e) {
                // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
                throw new UsernameNotFoundException("", e);
            }

            if (user == null) {
                throw new UsernameNotFoundException("");
            }else{
                if ( !user.getEnabled()){
                    throw new BadRequestException("账号未激活！");
                }
                jwtUserDto = new JwtUserDto(user, roleService.mapToGrantedAuthorities(user));
            }

            return jwtUserDto;
        }
}
