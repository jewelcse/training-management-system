package com.training.erp.repository;

import com.training.erp.entity.users.User;
import com.training.erp.entity.users.UserVerificationCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserVerificationCenterRepository extends JpaRepository<UserVerificationCenter,Long> {
    @Query(value = "SELECT * FROM user_verification_center WHERE verification_code =:code ",nativeQuery = true)
    UserVerificationCenter findByVerificationCode(@Param("code") String code);
    UserVerificationCenter findByUser(User user);
}
