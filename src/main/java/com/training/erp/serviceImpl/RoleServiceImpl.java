package com.training.erp.serviceImpl;
import com.training.erp.entity.users.Role;
import com.training.erp.entity.users.User;
import com.training.erp.repository.RoleRepository;
import com.training.erp.repository.UserRepository;
import com.training.erp.service.RoleService;
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
