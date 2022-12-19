package com.training.erp.controller;

import com.training.erp.entity.*;
import com.training.erp.exception.BatchNotFoundException;
import com.training.erp.exception.RoleNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.*;
import com.training.erp.model.response.LoginResponse;
import com.training.erp.model.response.MessageResponse;
import com.training.erp.model.response.UserProfileResponse;
import com.training.erp.repository.RoleRepository;
import com.training.erp.repository.UserVerificationCenterRepository;
import com.training.erp.security.jwt.JwtUtil;
import com.training.erp.service.BatchService;
import com.training.erp.service.CourseService;
import com.training.erp.service.RoleService;
import com.training.erp.service.UserService;
import com.training.erp.serviceImpl.UserDetailsImpl;
import com.training.erp.util.EmailService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private RoleService roleService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private BatchService batchService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserVerificationCenterRepository userVerificationCenterRepository;
    @Autowired
    private JwtUtil jwtUtil;

    // Login
    @PostMapping("/sign-in")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new LoginResponse(jwt, userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    // Sing up
    @PostMapping("/sign-up")
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterRequest request) throws RoleNotFoundException, MessagingException, UnsupportedEncodingException {

        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(request.getUsername() + " already exist!"));
        }
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(request.getEmail() + " is already in use!"));
        }
        // Create a new User Object
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        // Encode the password before set it to the user setPassword() method
        user.setPassword(encoder.encode(request.getPassword()));
        // User need an email verification
        // That's why setEnabled will be false
        // After successfully email verification it will be true
        user.setEnabled(false);
        String stringRole = request.getRole();
        Set<Role> roles = new HashSet<>();
        // Set a pointer
        boolean isTrainee = false;
        // Check the inputted role is null or not
        if (stringRole == null) {
            // If it is null, we will assign a default role that is ROLE_TRAINEE
            // Getting the role Object
            Role userRole = roleRepository.findByName(ERole.ROLE_TRAINEE)
                    .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " doesn't exist!"));
            roles.add(userRole);
            // Setting the account is true by default
            user.setNonLocked(true);
            isTrainee = true;
        } else {
            switch (stringRole) {
                case "ROLE_TRAINER":
                    Role trainerRole = roleRepository.findByName(ERole.ROLE_TRAINER)
                            .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINER + " doesn't exist!"));
                    roles.add(trainerRole);
                    // Trainer needs to be verified by email and
                    // Also needs to be activated by the admin
                    // That's why by default setNonLocked is false
                    // After activated by the admin it will be true
                    user.setNonLocked(false);
                    isTrainee = false;
                    break;
                case "ROLE_TRAINEE":
                    Role traineeRole = roleRepository.findByName(ERole.ROLE_TRAINEE)
                            .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " doesn't exist!"));

                    roles.add(traineeRole);
                    // By default, all trainees account will be activated
                    // They only needs to be verified only
                    // That's why setNonLocked is true by default
                    user.setNonLocked(true);
                    isTrainee = true;
                    break;
            }
        }
        user.setRoles(roles);
        userService.save(user, request, isTrainee);
        return ResponseEntity.ok(new MessageResponse("Registration Successfully completed, please check your mail to verify account!"));
    }

    // Add user
    //@CacheEvict(allEntries = true)
    @PostMapping("/users")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserAddRequest request) throws RoleNotFoundException, MessagingException, UnsupportedEncodingException {
        // Check the username is already used or not
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error:" + request.getEmail() + " is already in use!"));
        }

        // Generate username by the user firstName and lastName
        // With @ special character
        String randomUserName = request.getFirst_name().toLowerCase() + "@" + request.getLast_name().toLowerCase();
        // Generate a random password with length 8
        String randomUserPassword = RandomString.make(8);

        // Create a new user Object
        User user = new User();
        user.setUsername(randomUserName);
        user.setEmail(request.getEmail());
        // Encoded the password
        user.setPassword(encoder.encode(randomUserPassword));
        // Since the user is creating by admin
        // So, the account us verified and activated
        // No need to verify through email by user
        // And admin no need to activate the account
        user.setEnabled(true);
        user.setNonLocked(true);
        String stringRole = request.getRole();
        Set<Role> roles = new HashSet<>();
        boolean isTrainee = false;
        boolean isTrainer = false;
        if (stringRole == null) {
            Role userRole = roleRepository
                    .findByName(ERole.ROLE_TRAINEE)
                    .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " doesn't exist!"));

            roles.add(userRole);
            isTrainee = true;

        } else {
            switch (stringRole) {
                case "ROLE_ADMIN":
                    Role adminRole = roleRepository
                            .findByName(ERole.ROLE_ADMIN)
                            .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_ADMIN + " doesn't exist!"));
                    roles.add(adminRole);
                    break;

                case "ROLE_TRAINER":
                    Role studentRole = roleRepository
                            .findByName(ERole.ROLE_TRAINER)
                            .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINER + " doesn't exist!"));
                    roles.add(studentRole);
                    isTrainee = false;
                    isTrainer = true;
                    break;

                default:
                    Role userRole = roleRepository.findByName(ERole.ROLE_TRAINEE)
                            .orElseThrow(() -> new RoleNotFoundException(ERole.ROLE_TRAINEE + " doesn't exist!"));

                    roles.add(userRole);
                    isTrainee = true;
                    isTrainer = false;

            }
        }
        user.setRoles(roles);
        userService.saveUserByAdmin(user, request.getFirst_name(), request.getLast_name(), isTrainee, isTrainer);

        emailService.sendUserCredential(request.getEmail(), request.getFirst_name(), request.getLast_name(), randomUserName, randomUserPassword);
        return ResponseEntity.ok(new MessageResponse("Add user account successfully!"));
    }


    // Reset the user password
    @PostMapping("/users/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request, Principal principal) throws UserNotFoundException {
        // Check the user is exists or not
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Matches with the old_password and the encoded new_password
        // If not matched, then the old_password/ current password is invalid/incorrect
        if (!encoder.matches(request.getOld_password(), user.getPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body("Incorrect current password!");
        }

        // Matches the new_password with the confirm_password
        if (!request.getNew_password().equals(request.getConfirm_password())) {
            return ResponseEntity
                    .badRequest()
                    .body("Confirm password doesn't match with new password");
        }

        // Encode the password
        // Set the password to the user object
        user.setPassword(encoder.encode(request.getNew_password()));
        userService.resetPassword(user);
        return ResponseEntity.ok("Password reset successful");
    }

    // Get All the users
   // @Cacheable("users")
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }


    // Update the user
    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        userService.updateUser(userUpdateRequest);
        return ResponseEntity.ok("Updated Successfully!");
    }

    // Get user details by user ID
    @GetMapping("/users/{user-id}")
    public ResponseEntity<?> getUserById(@PathVariable("user-id") long userId) throws UserNotFoundException {
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));
        return ResponseEntity.ok(user);
    }

    // get the users corresponding trainee/trainer profile
    @GetMapping("/users/profile/{user-id}")
    public ResponseEntity<?> getUserProfileById(@PathVariable("user-id") long userId) throws UserNotFoundException {
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));
        // Implemented Factory Design pattern
        // If the role is trainee, then return the trainee profile by using the user object
        // Else return the Trainer profile
        if (user.getRoles().stream().anyMatch(role -> role.getName().toString().equals("ROLE_TRAINEE"))) {
            return ResponseEntity.ok(userService.getTraineeProfile(user));
        } else if (user.getRoles().stream().anyMatch(role -> role.getName().toString().equals("ROLE_TRAINER"))) {
            return ResponseEntity.ok(userService.getTrainerProfile(user));
        }
        // If it is not trainee or trainer
        // Then return only user profile by default
        return ResponseEntity.ok(userService.findById(userId));
    }

    // Get the user's complete profile with assigned batches and courses
    @GetMapping("/users/profile/v2/{user-id}")
    public ResponseEntity<UserProfileResponse<?>> getProfile(@PathVariable("user-id") long userId) throws UserNotFoundException, BatchNotFoundException {

        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found for id: " + userId));

        // Create an empty batch arrayList
        List<Batch> batches = new ArrayList<>();
        // Check the user is trainee or not
        // If it is trainee, then get the trainee profile
        if (user.getRoles().stream().anyMatch(role -> role.getName().toString().equals("ROLE_TRAINEE"))) {
            Trainee trainee = userService.getTraineeProfile(user);
            // If the trainee is not assigned any batch yet
            // Then add an empty batch
            if (trainee.getBatch() == null) {
                batches.add(new Batch());
            } else {
                // If the trainee already has assigned any batch
                // Get the batch profile by trainee
                Batch batch = batchService.getBatchByTrainee(trainee);
                batches.add(batch);
            }
            // Create the responses object
            UserProfileResponse<Trainee> response = new UserProfileResponse<>();
            response.setProfile(trainee);
            response.setUser(user);
            // Set Courses as null
            // Because trainees are not directly assigned any course
            // Trainees get all the courses by default what the batch contains
            response.setCourses(null);
            response.setBatches(batches);
            return ResponseEntity.ok(response);
        } else if (user.getRoles().stream().anyMatch(role -> role.getName().toString().equals("ROLE_TRAINER"))) {
            // Getting the trainer profile
            Trainer trainer = userService.getTrainerProfile(user);
            // Get the list of assigned courses of a trainer
            List<Course> courses = courseService.getCoursesByTrainerId(trainer.getId());
            // get the list of assigned batches of a trainer
            batches = batchService.getBatchesByTrainerId(trainer.getId());
            // Create the response object
            UserProfileResponse<Trainer> response = new UserProfileResponse<>();
            response.setUser(user);
            response.setProfile(trainer);
            response.setCourses(courses);
            response.setBatches(batches);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(null);
    }

    // Delete user
    @DeleteMapping("/users/{user-id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable("user-id") long userId) throws UserNotFoundException {
        // Check the user exists or not
        User user = userService
                .existsByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User Account Doesn't found for id: " + userId));
        // Delete the user by user ID
        userService.deleteUserById(user.getId());
        return ResponseEntity.ok(new MessageResponse("Removed Successfully!"));
    }


    // Activated/ Deactivated the user account
    @GetMapping("/activate-deactivate/user/account")
    public ResponseEntity<MessageResponse> activateDeActivateUserAccount(@RequestParam long trainerAccountId) throws UserNotFoundException {
        User user = userService
                .existsByUserId(trainerAccountId)
                .orElseThrow(() -> new UserNotFoundException("Trainer Account Doesn't found for id: " + trainerAccountId));
        userService.activateTrainerAccount(trainerAccountId, user);
        return ResponseEntity.ok(new MessageResponse("Action Completed"));
    }

    // Only Deactivate the user account
    @GetMapping("/deActivate/users/account")
    public ResponseEntity<MessageResponse> deActivateUserAccount(@RequestParam long userId) throws UserNotFoundException {
        User user = userService
                .existsByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("User Account Doesn't found for id: " + userId));
        // If the user is already activated
        if (!user.isNonLocked()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User account already deactivated!"));
        }
        userService.deActivateUserAccount(userId, user);
        return ResponseEntity.ok(new MessageResponse("Deactivated Successfully"));
    }

    // Verify account
    @GetMapping("/verify/account")
    public ResponseEntity<MessageResponse> verifyAccount(@RequestParam String code) throws UserNotFoundException {
        // Get the verification object by code
        UserVerificationCenter userVerificationCenter = userVerificationCenterRepository
                .findByVerificationCode(code);
        // Check the verification object is null or not
        if (userVerificationCenter == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Your verification code is not found in the Database!"));
        }
        // Check if the user is already verified or not
        if (userVerificationCenter.getUser().isEnabled()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Your account already verified!"));
        }
        // Get the current system time in milliseconds format
        Timestamp currentTimeMillis = new Timestamp(System.currentTimeMillis());
        //  Check the verification code is expired or not
        if (userVerificationCenter.getExpiryDate().before(currentTimeMillis)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Your verification code is expired, " +
                            "click resend button to get new code!"));
        }
        userService.verifyAccount(userVerificationCenter);
        return ResponseEntity.ok(new MessageResponse("Account verification successfully completed, Please login!"));
    }

    // Resend Code
    @PostMapping("/resend")
    public ResponseEntity<MessageResponse> resendCode(@RequestBody ResendCodeRequest request) throws UserNotFoundException, MessagingException, UnsupportedEncodingException {

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Email not found"));

        // Check the user is already verified  the account or not
        if (user.isEnabled()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Account already verified"));
        }

        // Get the verification object by user
        UserVerificationCenter userVerificationCenter = userVerificationCenterRepository.findByUser(user);

        // Check how many times the verification code is being requested or not
        // If the requested time is more than 3 times
        // The account can't be recovered
        // The user needs to be created a new account to use the system
        if (userVerificationCenter.getMaxLimit() <= 3 && userVerificationCenter.getMaxLimit() > 0) {
            userService.resendVerificationCode(userVerificationCenter, user);
        } else {
            user.setNonLocked(false);
            user.setEnabled(false);
            userService.lockedUserAccount(user);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Account locked, please create another account!"));
        }
        return ResponseEntity.ok(new MessageResponse("Code send to your email. please check your inbox!"));
    }

}
