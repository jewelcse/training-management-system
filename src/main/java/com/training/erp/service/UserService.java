package com.training.erp.service;

import com.training.erp.entity.*;
import com.training.erp.exception.RoleNotFoundException;
import com.training.erp.model.request.RegisterRequest;
import com.training.erp.model.request.UserAddRequest;
import com.training.erp.model.request.UserUpdateRequest;
import com.training.erp.model.response.RegisterResponse;
import com.training.erp.model.response.UserAddResponse;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String userName);
    boolean existsByEmail(String email);
    RegisterResponse save(RegisterRequest request) throws RoleNotFoundException, MessagingException, UnsupportedEncodingException;
    UserAddResponse save(UserAddRequest request) throws RoleNotFoundException, MessagingException, UnsupportedEncodingException;

    List<User> getAllUser();
    List<User> getAllUserByRole(ERole role);
    void deleteUserById(Long id);
    User updateUser(UserUpdateRequest userUpdateRequest);
    void activateDeactivateUserAccount(User user);
    Optional<User> existsByUserId(long trainerAccountId);
    Optional<User> findById(long userId);
    UserVerificationCenter verifyAccount(UserVerificationCenter userVerificationCenter);
    UserVerificationCenter resendVerificationCode(UserVerificationCenter userVerificationCenter, User user) throws MessagingException, UnsupportedEncodingException;
    List<Schedule> getAllScheduleByCourse(Course course);
    User resetPassword(User user);
    User lockedUserAccount(User user);
}
