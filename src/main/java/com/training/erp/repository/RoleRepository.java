package com.training.erp.repository;

import com.training.erp.entity.ERole;
import com.training.erp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(ERole roleName);
}
