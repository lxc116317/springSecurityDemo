package com.security.demo.security;

import com.security.demo.config.SecurityProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;


/**
 * JWT token提供器
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

    @Autowired
    private final SecurityProperties properties;
    public static final String AUTHORITIES_KEY = "auth";
    private JwtParser jwtParser;
    private JwtBuilder jwtBuilder;

    @Override
    public void afterPropertiesSet() throws Exception {

//        String secret = "ZmQ0ZGI5NjQ0MDQwY2I4MjMxY2Y3ZmI3MjdhN2ZmMjNhODViOTg1ZGE0NTBjMGM4NDA5NzYxMjdjOWMwYWRmZTBlZjlhNGY3ZTg4Y2U3YTE1ODVkZDU5Y2Y3OGYwZWE1NzUzNWQ2YjFjZDc0NGMxZWU2MmQ3MjY1NzJmNTE0MzI=";
        byte[] keyBytes = Decoders.BASE64.decode(properties.getBase64Secret());
        Key key = Keys.hmacShaKeyFor(keyBytes);
        jwtParser = Jwts.parserBuilder().setSigningKey(key).build();
        jwtBuilder = Jwts.builder().signWith(key, SignatureAlgorithm.HS512);

    }

    public String createToken(Authentication authentication){
        /**
         *  更优雅的字符串连接(join)收集器 Collectors.joining
         * https://blog.csdn.net/zebe1989/article/details/83054037
         */
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect
                (Collectors.joining(","));

        return jwtBuilder
                // 加入ID确保生成的 Token 都不一致
                .setId(UUID.randomUUID().toString())
                // 权限列表
                .claim(AUTHORITIES_KEY, authorities)
                // username
                .setSubject(authentication.getName())
                // 过期时间
                .setExpiration(DateUtils.addDays(new Date(), 1))
                .compact();
    }

    /**
     * 从token中获取认证信息
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        Claims claims = jwtParser.parseClaimsJws(token).getBody();
        Object authoritiesStr = claims.get(AUTHORITIES_KEY);
        Collection<? extends GrantedAuthority> authorities =
                authoritiesStr != null ?
                        Arrays.stream(authoritiesStr.toString().split(","))
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList()) : Collections.emptyList();
        User principal = new User(claims.getSubject(), "******", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }
}
