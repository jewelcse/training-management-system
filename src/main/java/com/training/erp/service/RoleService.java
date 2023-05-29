package com.training.erp.service;

import com.training.erp.entity.users.Role;
import com.training.erp.entity.users.User;

import java.util.List;

public interface RoleService {
    List<Role> getRoles();
    List<User> getUsersByRoleId(int roleId);
}
