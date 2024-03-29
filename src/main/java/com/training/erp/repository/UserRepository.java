package com.training.erp.repository;

import com.training.erp.entity.users.ERole;
import com.training.erp.entity.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String userName);
    boolean existsByEmail(String email);
    @Query("SELECT user FROM User user LEFT JOIN user.role role WHERE role.roleName =: role")
    List<User> findAllUsersByRole(ERole role);
    @Query("SELECT user FROM User user LEFT JOIN user.role role WHERE role.id = ?1")
    List<User> findUsersByRoleId(int role);
}
