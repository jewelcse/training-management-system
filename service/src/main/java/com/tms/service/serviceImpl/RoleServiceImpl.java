package com.tms.service.serviceImpl;

import com.tms.dto.request.*;
import com.tms.dto.response.*;
import com.tms.entity.*;
import com.tms.exception.*;
import com.tms.mapper.*;
import com.tms.repository.*;
import com.tms.service.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final UserRepository userRepository;
    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
    @Override
    public List<User> getUsersByRoleId(int roleId) {
        return userRepository.findUsersByRoleId(roleId);
    }

}
