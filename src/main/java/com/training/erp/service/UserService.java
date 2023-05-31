package com.training.erp.service;

import com.training.erp.entity.users.User;
import com.training.erp.entity.users.UserVerificationCenter;
import com.training.erp.model.request.RegisterRequest;
import com.training.erp.model.request.UserAddRequest;
import com.training.erp.model.request.UserUpdateRequest;
import com.training.erp.model.response.RegisterResponse;
import com.training.erp.model.response.UserAddResponse;
import com.training.erp.model.response.UserInfo;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String userName);
    boolean existsByEmail(String email);
    RegisterResponse save(RegisterRequest request);
    UserAddResponse save(UserAddRequest request);

    List<UserInfo> getAllUser();
    void deleteUserById(Long id);
    User updateUser(UserUpdateRequest userUpdateRequest);
    void activateDeactivateUserAccount(User user);
    Optional<User> existsByUserId(long id);
    Optional<User> findById(long id);
    UserVerificationCenter verifyAccount(UserVerificationCenter userVerificationCenter);
    UserVerificationCenter resendVerificationCode(UserVerificationCenter userVerificationCenter, User user) throws MessagingException, UnsupportedEncodingException;
    User resetPassword(User user);
    User lockedUserAccount(User user);
}
