package com.security.demo.service;

import com.security.demo.bean.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority("user:add"),
                new SimpleGrantedAuthority("user:view"),
                new SimpleGrantedAuthority("user:update"));

        User user = new User(1L, username, passwordEncoder.encode("123456"), true, authorities);

        if ( user == null ){
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        return user;
    }
}
