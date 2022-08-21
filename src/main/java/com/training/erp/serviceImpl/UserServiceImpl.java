package com.training.erp.serviceImpl;

import com.training.erp.entity.*;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.RegisterRequest;
import com.training.erp.model.request.UserUpdateRequest;
import com.training.erp.repository.*;
import com.training.erp.service.UserService;
import com.training.erp.util.EmailService;
import com.training.erp.util.UtilMethods;
import com.training.erp.util.UtilProperties;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.training.erp.util.UtilProperties.*;


@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private UserVerificationCenterRepository userVerificationCenterRepository;

    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    EmailService emailService;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);

    }
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);

    }
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public void save(User user, RegisterRequest request, boolean isTrainee) throws MessagingException, UnsupportedEncodingException {
        if (isTrainee){
            Trainee trainee = new Trainee();
            trainee.setUser(user);
            trainee.setFirstName(request.getFirst_name());
            trainee.setLastName(request.getLast_name());
            userRepository.save(user);
            traineeRepository.save(trainee);
        }else{
            Trainer trainer = new Trainer();
            trainer.setUser(user);
            trainer.setFirstName(request.getFirst_name());
            trainer.setLastName(request.getLast_name());
            userRepository.save(user);
            trainerRepository.save(trainer);

        }
        String randomCode = RandomString.make(CHARACTER_LIMIT_FOR_VERIFICATION_CODE);
        UserVerificationCenter userVerificationCenter = new UserVerificationCenter();
        userVerificationCenter.setUser(user);
        userVerificationCenter.setVerificationCode(randomCode);
        userVerificationCenter.setExpiryDate(UtilMethods.calculateExpiryDate(TIME_FOR_VERIFICATION_EXPIRATION));
        userVerificationCenter.setMaxLimit(MAX_LIMIT_FOR_VERIFICATION);
        userVerificationCenterRepository.save(userVerificationCenter);
        emailService.sendVerificationEmail(user,randomCode);
    }
    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }
    @Override
    public List<User> getAllUserByRole(ERole role) {
        return userRepository.findAllUsersByRole(role);
    }
    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
    @Transactional
    @Override
    public void updateUser(UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userUpdateRequest.getId())
                .orElseThrow(()-> new UsernameNotFoundException("user not found for id: "+userUpdateRequest.getId()));
        user.setId(user.getId());
        user.setEmail(userUpdateRequest.getEmail());
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(ERole.valueOf(userUpdateRequest.getRole()))
                        .orElseThrow(()-> new RuntimeException("Role not found for "+ userUpdateRequest.getRole()));
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
    }

    @Override
    public Optional<User> existsByUserId(long trainerAccountId) {
        return userRepository.findById(trainerAccountId);
    }
    @Override
    public void saveUserByAdmin(User user,String firstName,String lastName,boolean isTrainee, boolean isTrainer) {
        if (isTrainee && !isTrainer){
            Trainee trainee = new Trainee();
            trainee.setFirstName(firstName);
            trainee.setLastName(lastName);
            trainee.setUser(user);
            userRepository.save(user);
            traineeRepository.save(trainee);
        }else if (!isTrainee && isTrainer){
            Trainer trainer = new Trainer();
            trainer.setFirstName(firstName);
            trainer.setLastName(lastName);
            trainer.setUser(user);
            userRepository.save(user);
            trainerRepository.save(trainer);
        }else{
            userRepository.save(user);
        }
    }

    @Transactional
    @Override
    public void activateTrainerAccount(long trainerAccountId, User user) {
        user.setId(trainerAccountId);
        if(user.isNonLocked()){
            user.setNonLocked(false);
        }else{
            user.setNonLocked(true);
        }
        userRepository.save(user);
    }

    @Transactional
    @Override
    public void deActivateUserAccount(long userId, User user) {
        user.setId(userId);
        user.setNonLocked(false);
        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void verifyAccount(UserVerificationCenter verification) {
        verification.getUser().setId(verification.getUser().getId());
        verification.getUser().setEnabled(true);
        userVerificationCenterRepository.save(verification);
        userRepository.save(verification.getUser());
    }

    @Override
    public void resendVerificationCode(UserVerificationCenter userVerificationCenter, User user) throws MessagingException, UnsupportedEncodingException {
        String code = RandomString.make(CHARACTER_LIMIT_FOR_VERIFICATION_CODE);
        userVerificationCenter.setVerificationCode(code);
        userVerificationCenter.setMaxLimit(userVerificationCenter.getMaxLimit()-1);
        userVerificationCenter.setUser(user);
        userVerificationCenter.setExpiryDate(UtilMethods.calculateExpiryDate(TIME_FOR_VERIFICATION_EXPIRATION));
        userVerificationCenterRepository.save(userVerificationCenter);
        emailService.sendVerificationEmail(user,code);
    }

    @Override
    public Trainer getTrainerProfile(User user) {
        return trainerRepository.findByUser(user);
    }

    @Override
    public Trainee getTraineeProfile(User user) {
        return traineeRepository.findByUser(user);
    }

    @Override
    public List<Trainee> getTrainees() {
        return traineeRepository.getNotAssignedTrainees();
    }

    @Override
    public Optional<Trainee> getTraineeById(long traineeId) {
        return traineeRepository.findById(traineeId);
    }

    @Override
    public List<Trainer> getTrainers() {
        return trainerRepository.findAll();
    }

    @Override
    public Optional<Trainer> getTrainerById(long trainerId) {
        return trainerRepository.findById(trainerId);
    }
    @Override
    public List<Schedule> getAllScheduleByCourse(Course course) {
        return scheduleRepository.findAllByCourse(course);
    }
    @Override
    public void resetPassword(User user) {
        userRepository.save(user);
    }

    @Override
    public void lockedUserAccount(User user) {
        userRepository.save(user);
    }


}
