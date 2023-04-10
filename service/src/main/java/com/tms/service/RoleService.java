package com.tms.service;

import com.tms.dto.request.*;
import com.tms.dto.response.*;
import com.tms.entity.*;

import java.util.List;

public interface RoleService {
    List<Role> getRoles();
    List<User> getUsersByRoleId(int roleId);
}
