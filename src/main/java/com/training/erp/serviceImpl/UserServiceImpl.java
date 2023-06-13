package com.training.erp.serviceImpl;

import com.training.erp.entity.users.*;
import com.training.erp.exception.*;
import com.training.erp.mapper.UserMapper;
import com.training.erp.model.request.*;
import com.training.erp.model.response.*;
import com.training.erp.repository.*;
import com.training.erp.security.jwt.JwtUtil;
import com.training.erp.service.UserService;
import com.training.erp.util.EmailService;
import com.training.erp.util.UtilMethods;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.training.erp.util.UtilProperties.*;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final UserVerificationCenterRepository userVerificationCenterRepository;

    private final BCryptPasswordEncoder encoder;

    private final UserMapper userMapper;


    private final AuthenticationManager authenticationManager;

    private final JwtUtil jwtUtil;
    EmailService emailService;


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

    //@Transactional
    @Override
    public RegisterResponse save(RegisterRequest request){
        //user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setEnabled(false);
        user.setNonLocked(true);
        user.setPassword(encoder.encode(request.getPassword()));

        user.setRole(checkRoles(request.getRole()));

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
        user.setRole(checkRoles(request.getRole()));
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
    public List<UserInfo> getAllUser() {
        return userMapper.usersToUserInfoList(userRepository.findAll());
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
        Role role = roleRepository.findByRoleName(ERole.valueOf(userUpdateRequest.getRole()))
                .orElseThrow(() -> new RuntimeException("Role not found for " + userUpdateRequest.getRole()));
        roles.add(role);
        user.setRole(roles);
        return userRepository.save(user);
    }

    @Override
    public boolean existsByUserId(long trainerAccountId) {
        return !userRepository.existsById(trainerAccountId);
    }


    @Transactional
    @Override
    public void activateDeactivateUserAccount(Long userId) {
        User userDetails = userRepository.findById(userId).get();
        userDetails.setId(userDetails.getId());
        userDetails.setNonLocked(!userDetails.isNonLocked());
        userRepository.save(userDetails);
    }

    @Override
    public Optional<User> findById(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public void verifyAccount(AccountVerificationRequest request) {
        UserVerificationCenter verification = userVerificationCenterRepository
                .findByVerificationCode(request.getOtp());
        if (verification == null) {
            throw new UserAccountException("verification code not found");
        }
        if (verification.getUser().isEnabled()) {
            throw new UserAccountException("user account already verified");
        }

        Timestamp currentTimeMillis = new Timestamp(System.currentTimeMillis());
        if (verification.getOtpExpireAt().before(currentTimeMillis)) {
            throw new UserAccountException("expired otp");
        }
        verification.getUser().setId(verification.getUser().getId());
        verification.getUser().setEnabled(true);
        userRepository.save(verification.getUser());
        userVerificationCenterRepository.save(verification);
    }

    private void resendVerificationCode(UserVerificationCenter userVerificationCenter, User user) throws MessagingException, UnsupportedEncodingException {
        String code = RandomString.make(CHARACTER_LIMIT_FOR_VERIFICATION_CODE);
        userVerificationCenter.setOtp(code);
        userVerificationCenter.setMaxTries(userVerificationCenter.getMaxTries() - 1);
        userVerificationCenter.setUser(user);
        userVerificationCenter.setOtpExpireAt(UtilMethods.calculateExpiryDate(TIME_FOR_VERIFICATION_EXPIRATION));
        userVerificationCenterRepository.save(userVerificationCenter);
        emailService.sendVerificationEmail(user, code);
    }

    @Override
    public void resendOtp(User user) throws MessagingException, UnsupportedEncodingException {
        UserVerificationCenter userVerificationCenter = userVerificationCenterRepository.findByUser(user);
        if (userVerificationCenter.getMaxTries() <= 3 && userVerificationCenter.getMaxTries() > 0) {
            resendVerificationCode(userVerificationCenter, user);
        } else {
            user.setNonLocked(false);
            user.setEnabled(false);
            userRepository.save(user);
        }
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String generatedJwtToken = jwtUtil.generateToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return LoginResponse.builder()
                .token(generatedJwtToken)
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    public void resetPassword(PasswordResetRequest request, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("user not found"));
        if (!encoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new InputDataException("Incorrect the old password");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new InputDataException("New and confirm password are not matched");
        }
        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }


    @Override
    public UserDetails getUserDetails(long userId) {
        Optional<User> userDetails = userRepository.findById(userId);
        return userMapper.userToUserDetails(userDetails.get());
    }

    @Override
    public UserProfile getUserProfile(long userId) {
        Optional<User> userDetails = userRepository.findById(userId);
        return userMapper.userToUserProfile(userDetails.get());
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

    private Set<Role> checkRole(String stringRole){
        Set<Role> roles = new HashSet<>();
        if (stringRole == null) {
            Role defaultRole = roleRepository.findByRoleName(ERole.ROLE_TRAINEE)
                    .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " DOESN'T EXIST!"));
            roles.add(defaultRole);
        } else if(stringRole.equals(String.valueOf(ERole.ROLE_TRAINEE))){
            Role defaultRole = roleRepository.findByRoleName(ERole.ROLE_TRAINEE)
                    .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " DOESN'T EXIST!"));
            roles.add(defaultRole);
        }else if(stringRole.equals(String.valueOf(ERole.ROLE_TRAINER))){
            Role trainerRole = roleRepository.findByRoleName(ERole.ROLE_TRAINER)
                    .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINER + " DOESN'T EXIST!"));
            roles.add(trainerRole);
        }else{
            throw new RuntimeException("Not allowed!");
        }
        return roles;
    }


    private Set<Role> checkRoles(String stringRole){

        System.out.println("debug: 214 line in userserviceImpl" + stringRole);

        Set<Role> roles = new HashSet<>();
        if (stringRole == null) {
            Role defaultRole = roleRepository.findByRoleName(ERole.ROLE_TRAINEE)
                    .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " DOESN'T EXIST!"));
            roles.add(defaultRole);
        } else {
            switch (stringRole) {
                case "ROLE_TRAINER":
                    Role trainerRole = roleRepository.findByRoleName(ERole.ROLE_TRAINER)
                            .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINER + " DOESN'T EXIST!"));
                    roles.add(trainerRole);
                    break;
                case "ROLE_TRAINEE":
                    Role traineeRole = roleRepository.findByRoleName(ERole.ROLE_TRAINEE)
                            .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " DOESN'T EXIST!"));
                    roles.add(traineeRole);
                    break;
                default:
                    System.out.println("in default case");
                    throw new RoleNotAllowedException("Assigned role are not allowed!");
            }
        }
        return roles;
    }


}
