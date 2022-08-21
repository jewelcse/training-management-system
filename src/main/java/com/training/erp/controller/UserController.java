package com.training.erp.controller;


import com.training.erp.entity.*;
import com.training.erp.exception.BatchNotFoundException;
import com.training.erp.exception.RoleNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.UserUpdateRequest;
import com.training.erp.model.response.LoggedInUserProfileResponse;
import com.training.erp.model.response.MessageResponse;
import com.training.erp.model.response.TraineesProfileResponse;
import com.training.erp.model.response.TrainersProfileResponse;
import com.training.erp.repository.TrainerRepository;
import com.training.erp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
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
    @Autowired
    private TrainerRepository trainerRepository;

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
        if (user.getRoles().stream().anyMatch(role -> role.getName().toString().equals("ROLE_TRAINEE"))) {
            Trainee trainee = userService.getTraineeProfile(user);
            response.setProfile(trainee);
        } else if (user.getRoles().stream().anyMatch(role -> role.getName().toString().equals("ROLE_TRAINER"))) {
            Trainer trainer = userService.getTrainerProfile(user);
            response.setProfile(trainer);
        }

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

        Trainee trainee = userService.getTraineeProfile(user);
        Batch batch = null;
        List<Schedule> schedules;
        List<Course> courses = null;
        // When trainees are not assigned any batch
        // Create an empty batch
        if (trainee.getBatch() == null) {
            batch = new Batch();
            schedules = new ArrayList<>();
        } else {
            // If the trainee has assigned a batch
            batch = batchService.getBatchByTrainee(trainee);
            schedules = scheduleService.getAllScheduleByBatch(batch);
            courses = courseService.getCoursesByBatch(batch);
        }
        return ResponseEntity.ok(new TraineesProfileResponse(batch, courses, schedules));
    }

    // Get the trainers profile
    @GetMapping("/users/trainers/profile")
    public ResponseEntity<?> getTrainersProfile(Principal principal) throws UserNotFoundException {
        User user = userService
                .findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Trainer trainer = trainerRepository.findByUser(user);
        // check if batch is assigned or not
        // send list of courses
        // select schedules by batch
        List<Batch> batches = batchService.getBatchesByTrainerId(trainer.getId());
        List<Course> courses = courseService.getCoursesByTrainerId(trainer.getId());
        List<Schedule> schedules = scheduleService.getSchedulesByTrainerId(trainer);
        return ResponseEntity.ok(new TrainersProfileResponse(batches, courses, schedules));
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

    // Get all non assigned trainees
    @GetMapping("/trainees")
    public ResponseEntity<List<Trainee>> getNotAssignedTrainees() {
        return ResponseEntity.ok(userService.getTrainees());
    }

    // Get all trainers
    @GetMapping("/trainers")
    public ResponseEntity<List<Trainer>> getTrainers() {
        return ResponseEntity.ok(userService.getTrainers());
    }
}
