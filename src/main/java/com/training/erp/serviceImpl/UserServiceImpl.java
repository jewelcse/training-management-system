package com.training.erp.serviceImpl;

import com.training.erp.entity.*;
import com.training.erp.exception.RoleNotFoundException;
import com.training.erp.model.request.RegisterRequest;
import com.training.erp.model.request.UserAddRequest;
import com.training.erp.model.request.UserUpdateRequest;
import com.training.erp.model.response.RegisterResponse;
import com.training.erp.model.response.UserAddResponse;
import com.training.erp.repository.*;
import com.training.erp.service.UserService;
import com.training.erp.util.EmailService;
import com.training.erp.util.UtilMethods;
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
        user.setNonLocked(true);
        user.setPassword(encoder.encode(request.getPassword()));
        String stringRole = request.getRole();
        Set<Role> roles = new HashSet<>();
        // response
        RegisterResponse response = new RegisterResponse();
        response.setFirstName(request.getFirstName());
        response.setLastName(request.getLastName());
        response.setEmail(request.getEmail());
        response.setUsername(request.getUsername());
        if (stringRole == null) {
            Role defaultRole = roleRepository.findByName(ERole.ROLE_TRAINEE)
                    .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " doesn't exist!"));
            roles.add(defaultRole);
            response.setAccountVerified(false);
            response.setProfileType("TRAINEE ACCOUNT");
        } else {
            switch (stringRole) {
                case "ROLE_TRAINER":
                    Role trainerRole = roleRepository.findByName(ERole.ROLE_TRAINER)
                            .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINER + " doesn't exist!"));
                    roles.add(trainerRole);
                    response.setAccountVerified(false);
                    response.setProfileType("TRAINER ACCOUNT");
                    break;
                case "ROLE_TRAINEE":
                    Role traineeRole = roleRepository.findByName(ERole.ROLE_TRAINEE)
                            .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " doesn't exist!"));
                    roles.add(traineeRole);
                    response.setAccountVerified(false);
                    response.setProfileType("TRAINEE ACCOUNT");
                    break;
            }
        }
        user.setRoles(roles);
        Profile profile = new Profile();
        user.setUserProfile(profile);
        userRepository.save(user);
        // sent verification mail
        //todo: save profile while creating user account
        return response;
    }

    @Override
    public UserAddResponse save(UserAddRequest request) throws RoleNotFoundException, MessagingException, UnsupportedEncodingException {
        // generate random username and password
        String randomUserPassword = RandomString.make(8);
        String finalUsername = request.getFirstName().toLowerCase() + "@" + RandomString.make(4);

        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(finalUsername);
        user.setPassword(encoder.encode(randomUserPassword));
        user.setNonLocked(true);
        user.setEnabled(true);

        String stringRole = request.getRole();
        Set<Role> roles = new HashSet<>();

        if (stringRole == null) {
            Role defaultRole = roleRepository.findByName(ERole.ROLE_TRAINEE)
                    .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " doesn't exist!"));
            roles.add(defaultRole);
            user.setRoles(roles);
            userRepository.save(user);
        } else {
            switch (stringRole) {
                case "ROLE_TRAINER":
                    Role trainerRole = roleRepository.findByName(ERole.ROLE_TRAINER)
                            .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINER + " doesn't exist!"));
                    roles.add(trainerRole);
                    user.setRoles(roles);
                    userRepository.save(user);
                    break;
                case "ROLE_TRAINEE":
                    Role traineeRole = roleRepository.findByName(ERole.ROLE_TRAINEE)
                            .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " doesn't exist!"));
                    roles.add(traineeRole);
                    user.setRoles(roles);
                    userRepository.save(user);
                    break;
            }
        }
        UserAddResponse response = new UserAddResponse();
        response.setFirstName(request.getFirstName());
        response.setLastName(response.getLastName());
        response.setEmail(request.getEmail());
        response.setUsername(finalUsername);

        // sent main with credentials
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
    public User updateUser(UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userUpdateRequest.getId())
                .orElseThrow(()-> new UsernameNotFoundException("user not found for id: "+userUpdateRequest.getId()));
        user.setId(user.getId());
        user.setEmail(userUpdateRequest.getEmail());
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(ERole.valueOf(userUpdateRequest.getRole()))
                        .orElseThrow(()-> new RuntimeException("Role not found for "+ userUpdateRequest.getRole()));
        roles.add(role);
        user.setRoles(roles);
        return userRepository.save(user);
    }

    @Override
    public Optional<User> existsByUserId(long trainerAccountId) {
        return userRepository.findById(trainerAccountId);
    }


    @Transactional
    @Override
    public void activateDeactivateUserAccount(User user) {
        user.setId(user.getId());
        user.setNonLocked(!user.isNonLocked());
        userRepository.save(user);
    }

    @Override
    public Optional<User> findById(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public UserVerificationCenter verifyAccount(UserVerificationCenter verification) {
        verification.getUser().setId(verification.getUser().getId());
        verification.getUser().setEnabled(true);
        userRepository.save(verification.getUser());
        return userVerificationCenterRepository.save(verification);
    }

    @Override
    public UserVerificationCenter resendVerificationCode(UserVerificationCenter userVerificationCenter, User user) throws MessagingException, UnsupportedEncodingException {
        String code = RandomString.make(CHARACTER_LIMIT_FOR_VERIFICATION_CODE);
        userVerificationCenter.setVerificationCode(code);
        userVerificationCenter.setMaxLimit(userVerificationCenter.getMaxLimit()-1);
        userVerificationCenter.setUser(user);
        userVerificationCenter.setExpiryDate(UtilMethods.calculateExpiryDate(TIME_FOR_VERIFICATION_EXPIRATION));
        userVerificationCenterRepository.save(userVerificationCenter);
        emailService.sendVerificationEmail(user,code);
        return userVerificationCenter;
    }



    @Override
    public List<Schedule> getAllScheduleByCourse(Course course) {
        return scheduleRepository.findAllByCourse(course);
    }
    @Override
    public User resetPassword(User user) {
        return userRepository.save(user);
    }

    @Override
    public User lockedUserAccount(User user) {
        return userRepository.save(user);
    }

    private void sendEmail(){
        //        String randomCode = RandomString.make(CHARACTER_LIMIT_FOR_VERIFICATION_CODE);
//        UserVerificationCenter userVerificationCenter = new UserVerificationCenter();
//        userVerificationCenter.setUser(user);
//        userVerificationCenter.setVerificationCode(randomCode);
//        userVerificationCenter.setExpiryDate(UtilMethods.calculateExpiryDate(TIME_FOR_VERIFICATION_EXPIRATION));
//        userVerificationCenter.setMaxLimit(MAX_LIMIT_FOR_VERIFICATION);
//        userVerificationCenterRepository.save(userVerificationCenter);
//        emailService.sendVerificationEmail(user,randomCode);
    }

}
