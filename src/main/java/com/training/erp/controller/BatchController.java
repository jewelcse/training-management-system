package com.training.erp.controller;

import com.training.erp.entity.*;
import com.training.erp.exception.*;
import com.training.erp.model.request.BatchRequestDto;
import com.training.erp.model.request.UserAssignRequestDto;
import com.training.erp.model.response.BatchDetailsDto;
import com.training.erp.model.response.BatchFullProfileResponse;
import com.training.erp.model.response.MessageResponse;
import com.training.erp.service.BatchService;
import com.training.erp.service.CourseService;
import com.training.erp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BatchController {
    @Autowired
    private BatchService batchService;
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;

    @PostMapping("/batches")
    public ResponseEntity<MessageResponse> create(@RequestBody BatchRequestDto request){
        if(batchService.existsByBatchName(request.getBatchName())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(request.getBatchName() +" ALREADY EXIST!"));
        }
        batchService.save(request);
        return ResponseEntity.ok(new MessageResponse("BATCH CREATED SUCCESS!"));
    }

    @GetMapping("/batches")
    public ResponseEntity<List<Batch>> getBatches(){
        return ResponseEntity.ok(batchService.getAllBatch());
    }

    @GetMapping("/batches/{id}")
    public ResponseEntity<BatchDetailsDto> getBatch(@PathVariable("id") long batchId) throws BatchNotFoundException {
        return ResponseEntity.ok(batchService.getBatchById(batchId));
    }

    @GetMapping("/trainer/batches")
    public ResponseEntity<List<Batch>> getTrainerBatches(Principal principal) {
        User user = userService
                .findByUsername(principal.getName())
                .orElseThrow(()->new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(null);
    }

    // Get The batch full profile by batch ID
    // Get the Batch Name
    // Get the List of trainers
    // Get the List of Trainees
    // Get the List of courses
    @GetMapping("/batches/profile/{batch-id}")
    public ResponseEntity<BatchFullProfileResponse> getBatchProfileByBatchId(@PathVariable("batch-id") long batchId) {



        return ResponseEntity.ok(batchService.getBatchFullProfileByBatch(null));

    }

    // Remove Course from Batch
    @DeleteMapping("/batches/profile/{batch-id}/courses/{course-id}")
    public ResponseEntity<?> removeCourseFromBatch(@PathVariable("batch-id") long batchId, @PathVariable("course-id") long courseId){
        //Batch batch = batchService.getBatchById(batchId);
        Course course = courseService.getCourseByCourseId(courseId);
        //batchService.removeCourseFromBatch(batch,course);
        return ResponseEntity.ok("Course Removed Successfully");
    }

    // Remove trainers from Batch
    @DeleteMapping("/batches/profile/{batch-id}/trainers/{trainer-id}")
    public ResponseEntity<?> removeTrainersFromBatch(@PathVariable("batch-id") long batchId, @PathVariable("trainer-id") long trainerId){
//        Batch batch = batchService.getBatchById(batchId)
//                .orElseThrow(()-> new BatchNotFoundException("Batch not found"));

        return ResponseEntity.ok("Trainer Removed Successfully");
    }

    // Remove trainees from Batch
    @DeleteMapping("/batches/profile/{batch-id}/trainees/{trainee-id}")
    public ResponseEntity<?> removeTraineesFromBatch(@PathVariable("batch-id") long batchId, @PathVariable("trainee-id") long traineeId) throws BatchNotFoundException, CourseNotFoundException, TrainerNotFoundException, TraineeNotFoundException {
//        Batch batch = batchService.getBatchById(batchId)
//                .orElseThrow(()-> new BatchNotFoundException("Batch not found"));

        return ResponseEntity.ok("Trainee Removed Successfully");
    }
    @DeleteMapping("/batches/{batch-id}")
    public ResponseEntity<MessageResponse> deleteBatch(@PathVariable("batch-id") long batchId) {
        batchService.deleteBatchById(batchId);
        return ResponseEntity.ok(new MessageResponse("Batch remove successfully"));
    }

    @PostMapping("/batches/assign-user")
    public ResponseEntity<MessageResponse> assign(@RequestBody UserAssignRequestDto request) {
        batchService.assignUserToBatch(request);
        return ResponseEntity.ok(new MessageResponse("Assigned Success"));
    }

    @GetMapping("/assign/batches/{batch-id}/trainee/{trainee-id}")
    public ResponseEntity<MessageResponse> traineeAssign(@PathVariable("batch-id") long batchId, @PathVariable("trainee-id") long traineeId){

//        Batch batch = batchService.getBatchById(batchId)
//                .orElseThrow(()-> new BatchNotFoundException("Batch not found"));

        return ResponseEntity.ok(new MessageResponse("Assigned Success"));
    }

    @GetMapping("/assign/batches/{batch-id}/trainer/{trainer-id}")
    public ResponseEntity<MessageResponse> trainerAssign(@PathVariable("batch-id") long batchId, @PathVariable("trainer-id") long trainerId){

//        Batch batch = batchService.getBatchById(batchId)
//                .orElseThrow(()-> new BatchNotFoundException("Batch not found"));

        return ResponseEntity.ok(new MessageResponse("Assigned Success"));
    }




}
