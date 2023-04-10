package com.tms.controller;


import com.tms.entity.*;
import com.tms.exception.*;
import com.tms.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final RoleService roleService;

    // Get users by role
    @GetMapping("/users/by/role")
    public ResponseEntity<List<User>> getUsersByRole(@RequestParam("role") int roleId) throws RoleNotFoundException {
        return ResponseEntity.ok(roleService.getUsersByRoleId(roleId));
    }

    // Get all role
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }

  


}
