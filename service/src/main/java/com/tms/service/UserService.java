package com.tms.service;



import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;


import com.tms.dto.request.*;
import com.tms.dto.response.*;
import com.tms.entity.*;

public interface UserService {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String userName);
    boolean existsByEmail(String email);
    RegisterResponse save(RegisterRequest request);
    UserAddResponse save(UserAddRequest request);

    List<User> getAllUser();
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
