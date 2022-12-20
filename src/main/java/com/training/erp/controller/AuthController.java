package com.training.erp.controller;

import com.training.erp.entity.*;
import com.training.erp.exception.BatchNotFoundException;
import com.training.erp.exception.RoleNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.*;
import com.training.erp.model.response.LoginResponse;
import com.training.erp.model.response.MessageResponse;
import com.training.erp.model.response.RegisterResponse;
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
import org.springframework.http.HttpStatus;
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

        return new ResponseEntity<>(userService.save(request), HttpStatus.CREATED);
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserAddRequest request) throws RoleNotFoundException, MessagingException, UnsupportedEncodingException {
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error:" + request.getEmail() + " is already in use!"));
        }
        userService.save(request);
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
