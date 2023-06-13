package com.training.erp.controller;

import com.training.erp.entity.users.User;
import com.training.erp.exception.InputDataException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.*;
import com.training.erp.model.response.*;
import com.training.erp.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    @PostMapping("/signin")
    public ResponseEntity<LoginResponse> signIn(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody RegisterRequest request) {
        if (userService.existsByUsername(request.getUsername())) {
            throw new InputDataException("Username already exists");
        }
        if (userService.existsByEmail(request.getEmail())) {
            throw new InputDataException("Email already exists");
        }
        return new ResponseEntity<>(userService.save(request), HttpStatus.CREATED);
    }

    @PostMapping("/users")
    public ResponseEntity<?> addNewUser(@Valid @RequestBody UserAddRequest request){
        if (userService.existsByEmail(request.getEmail())) {
            throw new InputDataException("Email already exists");
        }
        userService.save(request);
        return ResponseEntity.ok(new MessageResponse("user added successfully"));
    }

    @PostMapping("/users/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetRequest request, Principal principal) {
        userService.resetPassword(request,principal);
        return ResponseEntity.ok(new MessageResponse("password reset success"));
    }

    // todo: add password reset limit

    @GetMapping("/users")
    public ResponseEntity<List<UserInfo>> getUsers() {
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(userService.updateUser(userUpdateRequest));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDetails> getUserDetails(@PathVariable("id") long userId){
        if (userService.existsByUserId(userId)){
            throw new UserNotFoundException("User not found");
        }
        return ResponseEntity.ok(userService.getUserDetails(userId));
    }

    @GetMapping("/users/profile/{id}")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable("id") long userId) throws UserNotFoundException {
        if (userService.existsByUserId(userId)){
            throw new UserNotFoundException("User not found");
        }
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }


    // todo: make only accessible for admin
    @DeleteMapping("/users/{id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable("id") long userId) {
        if (userService.existsByUserId(userId)){
            throw new UserNotFoundException("user not found");
        }
        userService.deleteUserById(userId);
        return ResponseEntity.ok(new MessageResponse("user deleted success"));
    }


    @PostMapping("/users/activate-deactivate/account")
    public ResponseEntity<MessageResponse> activateDeActivateUserAccount(@RequestParam long userId) {
        if (userService.existsByUserId(userId)){
            throw new UserNotFoundException("user not found");
        }
        userService.activateDeactivateUserAccount(userId);
        return ResponseEntity.ok(new MessageResponse("updated request"));
    }

    @PostMapping("/verify/account")
    public ResponseEntity<MessageResponse> verifyAccount(@RequestParam AccountVerificationRequest request) {
        userService.verifyAccount(request);
        return ResponseEntity.ok(new MessageResponse("account verified"));
    }

    @PostMapping("/resend")
    public ResponseEntity<MessageResponse> resendCode(@RequestBody ResendCodeRequest request) throws MessagingException, UnsupportedEncodingException {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("email not found"));

        if (user.isEnabled()) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("account already verified"));
        }
        userService.resendOtp(user);
        return ResponseEntity.ok(new MessageResponse("sent otp to " + request.getEmail()));
    }

}
