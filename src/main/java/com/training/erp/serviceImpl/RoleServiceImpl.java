package com.training.erp.serviceImpl;
import com.training.erp.entity.Role;
import com.training.erp.entity.User;
import com.training.erp.repository.RoleRepository;
import com.training.erp.repository.UserRepository;
import com.training.erp.service.RoleService;
import com.training.erp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
