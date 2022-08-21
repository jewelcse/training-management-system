package com.training.erp.controller;


import com.training.erp.entity.*;
import com.training.erp.exception.BatchNotFoundException;
import com.training.erp.exception.CourseNotFoundException;
import com.training.erp.exception.TrainerNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.CourseCreateRequest;
import com.training.erp.model.request.CourseUpdateRequest;
import com.training.erp.model.response.CourseFullProfileResponse;
import com.training.erp.model.response.MessageResponse;
import com.training.erp.repository.TrainerRepository;
import com.training.erp.service.AssignmentService;
import com.training.erp.service.BatchService;
import com.training.erp.service.CourseService;
import com.training.erp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private BatchService batchService;
    @Autowired
    private UserService userService;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private AssignmentService assignmentService;

    // Create Course
    //@Secured("ROLE_ADMIN")
    @PostMapping("/courses")
    public ResponseEntity<?> createCourse(@RequestBody CourseCreateRequest request) {

        if (courseService.existsByCourse(request.getCourse_name())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("The course already added!"));
        }
        courseService.createCourse(request);
        return new ResponseEntity<>(new MessageResponse("Course created successfully"), HttpStatus.CREATED);
    }


    // Get all courses
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getCourses() {
        return ResponseEntity.ok(courseService.getCourses());
    }

    // Get Trainers all course
    @GetMapping("/courses/trainer")
    public ResponseEntity<List<Course>> getCoursesByTrainerId(Principal principal) throws UserNotFoundException {
        User user = userService
                .findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Trainer trainer = trainerRepository.findByUser(user);

        return ResponseEntity.ok(courseService.getCoursesByTrainerId(trainer.getId()));
    }

    // Update Course
    @PutMapping("/courses")
    public ResponseEntity<?> updateCourse(@RequestBody CourseUpdateRequest request) throws UserNotFoundException, CourseNotFoundException {
        courseService.updateCourse(request);
        return new ResponseEntity<>(new MessageResponse("Course updated successfully"), HttpStatus.OK);
    }

    // Delete the course
    @DeleteMapping("/courses/{course-id}")
    public ResponseEntity<?> deleteCourseByCourseId(@PathVariable("course-id") long courseId) throws CourseNotFoundException {
        courseService.deleteCourseByCourseId(courseId);
        return ResponseEntity.ok(new MessageResponse("Course deleted successfully"));
    }

    // Get single course by course ID
    @GetMapping("/courses/{course-id}")
    public ResponseEntity<?> getCourseByCourseId(@PathVariable("course-id") long courseId) throws CourseNotFoundException {
        return ResponseEntity.ok(courseService.getCourseByCourseId(courseId));
    }

    // Get Course full profile by course ID
    // Get The course details
    // Get Assigned trainers details
    // Get the list of assignments
    @GetMapping("/courses/profile/{course-id}")
    public ResponseEntity<?> getCourseProfileByCourseId(@PathVariable("course-id") long courseId) throws CourseNotFoundException {

        Course course = courseService.getCourseByCourseId(courseId);
        Trainer trainer;
        if (course.getTrainer() == null) {
            trainer = new Trainer();
        } else {
            trainer = courseService.getTrainerProfileByCourse(course);
        }

        List<Assignment> assignments = assignmentService.getAssignmentsByCourse(course.getId());

        return ResponseEntity.ok(new CourseFullProfileResponse(course, trainer, assignments));
    }

    // Assign trainer to course
    @GetMapping("/courses/{course-id}/trainers/{trainer-id}")
    public ResponseEntity<?> assignTrainerToCourse(@PathVariable("course-id") long courseId, @PathVariable("trainer-id") long trainerId) throws CourseNotFoundException, TrainerNotFoundException {
        Course course = courseService.getCourseByCourseId(courseId);
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not Found"));
        course.setTrainer(trainer);
        courseService.updateCourseTrainer(course);
        return ResponseEntity.ok(new MessageResponse("Trainer Assigned Success"));
    }

    // Add course to batch
    @GetMapping("/courses/{course-id}/batches/{batch-id}")
    public ResponseEntity<?> addCourseToBatch(@PathVariable("course-id") long courseId, @PathVariable("batch-id") long batchId) throws CourseNotFoundException, BatchNotFoundException {
        Course course = courseService.getCourseByCourseId(courseId);
        Batch batch = batchService.getBatchById(batchId)
                .orElseThrow(() -> new BatchNotFoundException("Batch no found"));
        Set<Course> courses = batch.getCourses();
        courses.add(course);
        batch.setCourses(courses);
        batchService.updateBatch(batch);
        return ResponseEntity.ok(new MessageResponse("Course Added Success"));
    }

    // Get courses by batch ID
    @GetMapping("/courses/batches/{batch-id}")
    public ResponseEntity<List<Course>> getCoursesByBatchId(@PathVariable("batch-id") long batchId) throws BatchNotFoundException {
        Batch batch = batchService.getBatchById(batchId)
                .orElseThrow(() -> new BatchNotFoundException("Batch no found"));
        return ResponseEntity.ok(courseService.getCoursesByBatch(batch));
    }

    // Remove Trainer from Course
    @DeleteMapping("/courses/{course-id}/trainers/{trainer-id}")
    public ResponseEntity<?> removeTrainerFromCourse(@PathVariable("course-id") long courseId, @PathVariable("trainer-id") long trainerId) throws CourseNotFoundException, TrainerNotFoundException {
        Course course = courseService.getCourseByCourseId(courseId);
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(() -> new TrainerNotFoundException("Trainer not Found"));
        // check either the trainer is assigned before to the course or not
        if (course.getTrainer() != trainer) {
            return ResponseEntity
                    .badRequest()
                    .body("Trainer not found For This course");
        }
        courseService.removeTrainerFromCourse(course);
        return ResponseEntity.ok(new MessageResponse("Trainer Remove Success"));
    }


}
