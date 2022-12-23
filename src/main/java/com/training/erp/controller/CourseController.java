package com.training.erp.controller;


import com.training.erp.model.request.AddTrainerToCourseRequest;
import com.training.erp.model.request.CourseCreateRequest;
import com.training.erp.model.request.CourseUpdateRequest;
import com.training.erp.model.request.RemoveTrainerRequest;
import com.training.erp.model.response.CourseDetails;
import com.training.erp.model.response.CourseResponse;
import com.training.erp.model.response.MessageResponse;
import com.training.erp.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CourseController {
    private final CourseService courseService;

    @PostMapping("/courses")
    public ResponseEntity<CourseResponse> create(@RequestBody CourseCreateRequest request) {
        return new ResponseEntity<>(courseService.save(request), HttpStatus.CREATED);
    }

    @PutMapping("/courses")
    public ResponseEntity<CourseResponse> update(@RequestBody CourseUpdateRequest request){
        return new ResponseEntity<>(courseService.update(request), HttpStatus.OK);
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseResponse>> getCourses() {
        return ResponseEntity.ok(courseService.getCourses());
    }

    @DeleteMapping("/courses/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") long id){
        courseService.deleteCourseById(id);
        return ResponseEntity.ok(new MessageResponse("Course remove successfully"));
    }

    @GetMapping("/courses/{id}")
    public ResponseEntity<CourseDetails> getCourse(@PathVariable("id") long id){
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PostMapping("/courses/add-trainer")
    public ResponseEntity<MessageResponse> addTrainer(@RequestBody AddTrainerToCourseRequest request){
        return new ResponseEntity<>(courseService.addTrainerToCourse(request), HttpStatus.ACCEPTED);
    }

    @PostMapping("/courses/remove-trainer")
    public ResponseEntity<MessageResponse> removeTrainer(@RequestBody RemoveTrainerRequest request){
        return new ResponseEntity<>(courseService.removeTrainerFromCourse(request), HttpStatus.ACCEPTED);
    }


}
