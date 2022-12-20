package com.training.erp.controller;


import com.training.erp.entity.*;
import com.training.erp.exception.BatchNotFoundException;
import com.training.erp.exception.RoleNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.response.LoggedInUserProfileResponse;
import com.training.erp.model.response.TraineesProfileResponse;
import com.training.erp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BatchService batchService;
    @Autowired
    private CourseService courseService;
    @Autowired
    private ScheduleService scheduleService;


    // Get Logged in user profile
    @GetMapping("/users/profile")
    public ResponseEntity<?> loggedInUserProfile() {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for username "));

        // Factory Design pattern is implemented
        LoggedInUserProfileResponse response = new LoggedInUserProfileResponse();
        response.setUser(user);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/trainees/profile")
    public ResponseEntity<TraineesProfileResponse> getTraineesProfile() throws BatchNotFoundException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        }
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for username "));


        return ResponseEntity.ok(null);
    }

    // Get the trainers profile
    @GetMapping("/users/trainers/profile")
    public ResponseEntity<?> getTrainersProfile(Principal principal) throws UserNotFoundException {
        User user = userService
                .findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(null);
    }

    // Get users by role
    @GetMapping("/users/by/role")
    public ResponseEntity<List<User>> getUsersByRole(@RequestParam("role") int roleId) throws RoleNotFoundException {
        return ResponseEntity.ok(roleService.getUsersByRoleId(roleId));
    }

    // Get all role
    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleService.getRoles());
    }

  


}
