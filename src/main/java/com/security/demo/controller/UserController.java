package com.security.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

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
}
