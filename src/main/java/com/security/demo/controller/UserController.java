package com.security.demo.controller;

import com.security.demo.bean.UserBean;
import com.security.demo.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('user:add')")
    public String add() {
        return "user:add";
    }

    @GetMapping("/update")
    @PreAuthorize("hasAnyRole('user:update')")
    public String update() {
        return "user:update";
    }

    @GetMapping("/view")
    @PreAuthorize("hasAnyRole('user:view')")
    public String view() {
        return "user:view";
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('user:delete')")
    public String delete() {
        return "user:delete";
    }



    @ApiOperation("新增用户")
    @PostMapping
    @PreAuthorize("@el.check('user:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody UserBean resources){
//        checkLevel(resources);
        // 默认密码 123456
        resources.setPassword(passwordEncoder.encode("123456"));
        userService.add(resources);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
