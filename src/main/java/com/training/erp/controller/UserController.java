package com.training.erp.controller;


import com.training.erp.entity.*;
import com.training.erp.exception.RoleNotFoundException;
import com.training.erp.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
