package com.training.erp.service;

import com.training.erp.entity.ERole;
import com.training.erp.entity.Role;
import com.training.erp.entity.User;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getRoles();
    List<User> getUsersByRoleId(int roleId);
}
