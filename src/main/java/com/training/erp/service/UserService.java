package com.training.erp.service;

import com.training.erp.entity.*;
import com.training.erp.exception.RoleNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.RegisterRequest;
import com.training.erp.model.request.UserUpdateRequest;
import com.training.erp.model.response.RegisterResponse;

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
    List<User> getAllUser();
    List<User> getAllUserByRole(ERole role);
    void deleteUserById(Long id);
    void updateUser(UserUpdateRequest userUpdateRequest);
    void activateTrainerAccount(long trainerAccountId,User user);
    Optional<User> existsByUserId(long trainerAccountId);
    void saveUserByAdmin(User user,String firstName,String lastName,boolean isTrainee, boolean isTrainer);
    void deActivateUserAccount(long userId, User user);
    Optional<User> findById(long userId);
    void verifyAccount(UserVerificationCenter userVerificationCenter);
    void resendVerificationCode(UserVerificationCenter userVerificationCenter, User user) throws MessagingException, UnsupportedEncodingException;
    Trainer getTrainerProfile(User user);
    Trainee getTraineeProfile(User user);
    List<Trainee> getTrainees();
    Optional<Trainee> getTraineeById(long traineeId);
    List<Trainer> getTrainers();
    Optional<Trainer> getTrainerById(long trainerId);
    List<Schedule> getAllScheduleByCourse(Course course);
    void resetPassword(User user);
    void lockedUserAccount(User user);
}
