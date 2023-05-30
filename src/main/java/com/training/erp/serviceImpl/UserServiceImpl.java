package com.training.erp.serviceImpl;

import com.training.erp.entity.users.*;
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
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
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
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserVerificationCenterRepository userVerificationCenterRepository;

    private final BCryptPasswordEncoder encoder;
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
    public RegisterResponse save(RegisterRequest request) {
        //user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setEnabled(false);
        user.setNonLocked(true);
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRoles(checkRoles(request.getRole()));
        //profile
        Profile profile = new Profile();
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());

        user.setProfile(profile);
        userRepository.save(user);
        //todo: sent verification mail
        //response
        return RegisterResponse.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .isAccountNonLocked(true)
                .isAccountVerified(false) // enabled is renamed here with verified attribute
                .build();
    }

    @Override
    public UserAddResponse save(UserAddRequest request) {
        //generate random username and password
        String randomUserPassword = RandomString.make(8);
        String randomUsername = request.getFirstName().toLowerCase() + "@" + RandomString.make(4);
        //user
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(randomUsername);
        user.setPassword(encoder.encode(randomUserPassword));
        user.setNonLocked(true);
        user.setEnabled(true);
        user.setRoles(checkRoles(request.getRole()));
        //profile
        Profile profile = new Profile();
        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());

        user.setProfile(profile);
        userRepository.save(user);
        //todo: sent mail with credentials
        return UserAddResponse
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(randomUsername)
                .build();

    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public User updateUser(UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(userUpdateRequest.getId())
                .orElseThrow(() -> new UsernameNotFoundException("user not found for id: " + userUpdateRequest.getId()));
        user.setId(user.getId());
        user.setEmail(userUpdateRequest.getEmail());
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName(ERole.valueOf(userUpdateRequest.getRole()))
                .orElseThrow(() -> new RuntimeException("Role not found for " + userUpdateRequest.getRole()));
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
        userVerificationCenter.setMaxLimit(userVerificationCenter.getMaxLimit() - 1);
        userVerificationCenter.setUser(user);
        userVerificationCenter.setExpiryDate(UtilMethods.calculateExpiryDate(TIME_FOR_VERIFICATION_EXPIRATION));
        userVerificationCenterRepository.save(userVerificationCenter);
        emailService.sendVerificationEmail(user, code);
        return userVerificationCenter;
    }

    @Override
    public User resetPassword(User user) {
        return userRepository.save(user);
    }

    @Override
    public User lockedUserAccount(User user) {
        return userRepository.save(user);
    }

    private void sendEmail() {
        //        String randomCode = RandomString.make(CHARACTER_LIMIT_FOR_VERIFICATION_CODE);
//        UserVerificationCenter userVerificationCenter = new UserVerificationCenter();
//        userVerificationCenter.setUser(user);
//        userVerificationCenter.setVerificationCode(randomCode);
//        userVerificationCenter.setExpiryDate(UtilMethods.calculateExpiryDate(TIME_FOR_VERIFICATION_EXPIRATION));
//        userVerificationCenter.setMaxLimit(MAX_LIMIT_FOR_VERIFICATION);
//        userVerificationCenterRepository.save(userVerificationCenter);
//        emailService.sendVerificationEmail(user,randomCode);
    }

    private Set<Role> checkRoles(String stringRole) throws RoleNotFoundException {
        Set<Role> roles = new HashSet<>();
        if (stringRole == null) {
            Role defaultRole = roleRepository.findByName(ERole.ROLE_TRAINEE)
                    .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " DOESN'T EXIST!"));
            roles.add(defaultRole);
        } else {
            switch (stringRole) {
                case "ROLE_TRAINER":
                    Role trainerRole = roleRepository.findByName(ERole.ROLE_TRAINER)
                            .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINER + " DOESN'T EXIST!"));
                    roles.add(trainerRole);
                    break;
                case "ROLE_TRAINEE":
                    Role traineeRole = roleRepository.findByName(ERole.ROLE_TRAINEE)
                            .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " DOESN'T EXIST!"));
                    roles.add(traineeRole);
                    break;
            }
        }
        return roles;
    }


}
