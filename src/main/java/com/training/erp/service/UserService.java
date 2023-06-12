package com.training.erp.service;

import com.training.erp.entity.users.User;
import com.training.erp.model.request.*;
import com.training.erp.model.response.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String userName);
    boolean existsByEmail(String email);
    RegisterResponse save(RegisterRequest request);
    UserAddResponse save(UserAddRequest request);

    List<UserInfo> getAllUser();
    void deleteUserById(Long id);
    User updateUser(UserUpdateRequest userUpdateRequest);
    void activateDeactivateUserAccount(Long userId);
    boolean existsByUserId(long id);
    Optional<User> findById(long id);
    void verifyAccount(AccountVerificationRequest request);
    void resetPassword(PasswordResetRequest request, Principal principal);

    UserDetails getUserDetails(long userId);

    UserProfile getUserProfile(long userId);

    void resendOtp(User user) throws MessagingException, UnsupportedEncodingException;

    LoginResponse login(LoginRequest loginRequest);


}
