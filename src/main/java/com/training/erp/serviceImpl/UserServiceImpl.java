package com.training.erp.serviceImpl;

import com.training.erp.entity.*;
import com.training.erp.exception.RoleNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.RegisterRequest;
import com.training.erp.model.request.UserUpdateRequest;
import com.training.erp.model.response.RegisterResponse;
import com.training.erp.repository.*;
import com.training.erp.service.UserService;
import com.training.erp.util.EmailService;
import com.training.erp.util.UtilMethods;
import com.training.erp.util.UtilProperties;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private BCryptPasswordEncoder encoder;
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
    public RegisterResponse save(RegisterRequest request) throws RoleNotFoundException, MessagingException, UnsupportedEncodingException {
        // user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setEnabled(false);
        user.setPassword(encoder.encode(request.getPassword()));
        String stringRole = request.getRole();
        Set<Role> roles = new HashSet<>();
        // response
        RegisterResponse response = new RegisterResponse();
        response.setFirstName(request.getFirst_name());
        response.setLastName(request.getLast_name());
        response.setEmail(request.getEmail());
        response.setUsername(request.getUsername());
        if (stringRole == null) {
            Role defaultRole = roleRepository.findByName(ERole.ROLE_TRAINEE)
                    .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " doesn't exist!"));
            roles.add(defaultRole);
            user.setRoles(roles);
            user.setNonLocked(true);
            response.setAccountLocked(false);
            response.setAccountVerified(false);
            userRepository.save(user);
            saveTrainee(request.getFirst_name(),request.getLast_name(),user);
        } else {
            switch (stringRole) {
                case "ROLE_TRAINER":
                    Role trainerRole = roleRepository.findByName(ERole.ROLE_TRAINER)
                            .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINER + " doesn't exist!"));
                    roles.add(trainerRole);
                    user.setRoles(roles);
                    user.setNonLocked(false);
                    response.setAccountLocked(true);
                    response.setAccountVerified(false);
                    userRepository.save(user);
                    saveTrainer(request.getFirst_name(),request.getLast_name(),user);
                    break;
                case "ROLE_TRAINEE":
                    Role traineeRole = roleRepository.findByName(ERole.ROLE_TRAINEE)
                            .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " doesn't exist!"));
                    roles.add(traineeRole);
                    user.setRoles(roles);
                    user.setNonLocked(true);
                    response.setAccountLocked(false);
                    response.setAccountVerified(false);
                    userRepository.save(user);
                    saveTrainee(request.getFirst_name(),request.getLast_name(),user);
                    break;
            }
        }
        String randomCode = RandomString.make(CHARACTER_LIMIT_FOR_VERIFICATION_CODE);
        UserVerificationCenter userVerificationCenter = new UserVerificationCenter();
        userVerificationCenter.setUser(user);
        userVerificationCenter.setVerificationCode(randomCode);
        userVerificationCenter.setExpiryDate(UtilMethods.calculateExpiryDate(TIME_FOR_VERIFICATION_EXPIRATION));
        userVerificationCenter.setMaxLimit(MAX_LIMIT_FOR_VERIFICATION);
        userVerificationCenterRepository.save(userVerificationCenter);
        emailService.sendVerificationEmail(user,randomCode);
        return response;
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

    private Trainee saveTrainee(String firstName, String lastName, User user) {
        Trainee trainee = new Trainee();
        trainee.setFirstName(firstName);
        trainee.setLastName(lastName);
        trainee.setUser(user);
        return traineeRepository.save(trainee);
    }

    private Trainer saveTrainer(String firstName, String lastName, User user){
        Trainer trainer = new Trainer();
        trainer.setFirstName(firstName);
        trainer.setLastName(lastName);
        trainer.setUser(user);
        return trainerRepository.save(trainer);
    }

}
