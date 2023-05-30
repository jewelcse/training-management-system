package com.training.erp.controller;

import com.training.erp.entity.users.User;
import com.training.erp.entity.users.UserVerificationCenter;
import com.training.erp.exception.RoleNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.*;
import com.training.erp.model.response.LoginResponse;
import com.training.erp.model.response.MessageResponse;
import com.training.erp.model.response.UserDetails;
import com.training.erp.model.response.UserProfile;
import com.training.erp.repository.UserVerificationCenterRepository;
import com.training.erp.security.jwt.JwtUtil;
import com.training.erp.service.UserService;
import com.training.erp.serviceImpl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserVerificationCenterRepository userVerificationCenterRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> signIn(@Valid @RequestBody LoginRequest loginRequest) {
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

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody RegisterRequest request) throws RoleNotFoundException, MessagingException, UnsupportedEncodingException {

        if (userService.existsByUsername(request.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(request.getUsername() + " ALREADY EXISTS!"));
        }
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(request.getEmail() + " IS ALREADY IN USE!"));
        }

        return new ResponseEntity<>(userService.save(request), HttpStatus.CREATED);
    }

    @PostMapping("/users")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserAddRequest request) throws RoleNotFoundException, MessagingException, UnsupportedEncodingException {
        if (userService.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(request.getEmail() + " IS ALREADY IN USE!"));
        }
        userService.save(request);
        return ResponseEntity.ok(new MessageResponse("USER ADD SUCCESSFUL!"));
    }

    @PostMapping("/users/password-reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request, Principal principal) throws UserNotFoundException {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"));

        if (!encoder.matches(request.getOld_password(), user.getPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body("INCORRECT THE CURRENT PASSWORD!");
        }

        if (!request.getNew_password().equals(request.getConfirm_password())) {
            return ResponseEntity
                    .badRequest()
                    .body("CONFIRM PASSWORD DOESN'T MATCH WITH THE NEW PASSWORD!");
        }
        user.setPassword(encoder.encode(request.getNew_password()));
        userService.resetPassword(user);
        return ResponseEntity.ok("PASSWORD RESET SUCCESSFUL!");
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(userService.updateUser(userUpdateRequest));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDetails> getUser(@PathVariable("id") long userId) throws UserNotFoundException {
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND!"));
        UserDetails details = UserDetails
                .builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .isNonLocked(user.isNonLocked())
                .isEnabled(user.isEnabled())
                .roles(user.getRole())
                .build();
        return ResponseEntity.ok(details);
    }

    @GetMapping("/users/profile/{id}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable("id") long userId) throws UserNotFoundException {
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND!"));
        UserProfile profile = UserProfile
                .builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .isEnabled(user.isEnabled())
                .isNonLocked(user.isNonLocked())
                .firstName(user.getProfile().getFirstName())
                .lastName(user.getProfile().getLastName())
                .phoneNumber(user.getProfile().getPhone())
                .street(user.getProfile().getStreet())
                .state(user.getProfile().getState())
                .zipCode(user.getProfile().getZipCode())
                .gender(user.getProfile().getGender())
                .city(user.getProfile().getCity())
                .country(user.getProfile().getCountry())
                .dateOfBirth(user.getProfile().getDob())
                .address1(user.getProfile().getAddress1())
                .address2(user.getProfile().getAddress2())
                .roles(user.getRole())
                .build();

        return new ResponseEntity<>(profile, HttpStatus.OK);
    }


    @DeleteMapping("/users/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable("id") long userId) throws UserNotFoundException {
        User user = userService
                .existsByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND!"));
        userService.deleteUserById(user.getId());
        return ResponseEntity.ok(new MessageResponse("USER REMOVED SUCCESSFUL!"));
    }


    @GetMapping("/activate-deactivate/user/account")
    public ResponseEntity<MessageResponse> activateDeActivateUserAccount(@RequestParam long id) throws UserNotFoundException {
        User user = userService
                .existsByUserId(id)
                .orElseThrow(() -> new UserNotFoundException("USER NOT FOUND!"));
        userService.activateDeactivateUserAccount(user);
        return ResponseEntity.ok(new MessageResponse("UPDATED!"));
    }

    @GetMapping("/verify/account")
    public ResponseEntity<MessageResponse> verifyAccount(@RequestParam String code) {
        UserVerificationCenter userVerificationCenter = userVerificationCenterRepository
                .findByVerificationCode(code);
        if (userVerificationCenter == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("VERIFICATION CODE NOT FOUND!"));
        }
        if (userVerificationCenter.getUser().isEnabled()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ACCOUNT ALREADY VERIFIED!"));
        }
        Timestamp currentTimeMillis = new Timestamp(System.currentTimeMillis());
        if (userVerificationCenter.getOtpExpireAt().before(currentTimeMillis)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("VERIFICATION CODE EXPIRED!"));
        }
        userService.verifyAccount(userVerificationCenter);
        return ResponseEntity.ok(new MessageResponse("ACCOUNT VERIFICATION SUCCESSFUL, PLEASE LOGIN!"));
    }

    @PostMapping("/resend")
    public ResponseEntity<MessageResponse> resendCode(@RequestBody ResendCodeRequest request) throws UserNotFoundException, MessagingException, UnsupportedEncodingException {

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("EMAIL NOT FOUND!"));

        if (user.isEnabled()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ACCOUNT ALREADY VERIFIED!"));
        }
        UserVerificationCenter userVerificationCenter = userVerificationCenterRepository.findByUser(user);
        if (userVerificationCenter.getMaxTries() <= 3 && userVerificationCenter.getMaxTries() > 0) {
            userService.resendVerificationCode(userVerificationCenter, user);
        } else {
            user.setNonLocked(false);
            user.setEnabled(false);
            userService.lockedUserAccount(user);
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ACCOUNT LOCKED!"));
        }
        return ResponseEntity.ok(new MessageResponse("CODE SENT TO YOUR MAIL!"));
    }

}
