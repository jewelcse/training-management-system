package com.training.erp.controller;

import com.training.erp.entity.*;
import com.training.erp.exception.*;
import com.training.erp.model.request.BatchRequestDto;
import com.training.erp.model.request.UserAssignRequestDto;
import com.training.erp.model.response.BatchFullProfileResponse;
import com.training.erp.model.response.MessageResponse;
import com.training.erp.repository.TraineeRepository;
import com.training.erp.repository.TrainerRepository;
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
    private TrainerRepository trainerRepository;
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private CourseService courseService;
    // Create batch
    @PostMapping("/batches")
    public ResponseEntity<MessageResponse> createBatch(@RequestBody BatchRequestDto request){
        // Check the batch is already added or not
        if(batchService.existsByBatchName(request.getBatch_name())){
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(request.getBatch_name() +" already exist!"));
        }
        batchService.createNewBatch(request);
        return ResponseEntity.ok(new MessageResponse("Training Batch created successfully"));
    }

    // Get Batches
    @GetMapping("/batches")
    public ResponseEntity<List<Batch>> getBatches(){
        return ResponseEntity.ok(batchService.getAllBatch());
    }

    // Get batch by batch ID
    @GetMapping("/batches/{batch-id}")
    public ResponseEntity<Batch> getBatch(@PathVariable("batch-id") long batchId) throws BatchNotFoundException {

        Batch batch = batchService.getBatchById(batchId)
                .orElseThrow(()-> new BatchNotFoundException("Batch not found"));

        return ResponseEntity.ok(batch);
    }

    // Get the trainers batches
    @GetMapping("/trainer/batches")
    public ResponseEntity<List<Batch>> getTrainerBatches(Principal principal) {
        User user = userService
                .findByUsername(principal.getName())
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
        Trainer trainer = trainerRepository.findByUser(user);
        return ResponseEntity.ok(batchService.getBatchesByTrainerId(trainer.getId()));
    }

    // Get The batch full profile by batch ID
    // Get the Batch Name
    // Get the List of trainers
    // Get the List of Trainees
    // Get the List of courses
    @GetMapping("/batches/profile/{batch-id}")
    public ResponseEntity<BatchFullProfileResponse> getBatchProfileByBatchId(@PathVariable("batch-id") long batchId) throws BatchNotFoundException {

        Batch batch = batchService.getBatchById(batchId)
                .orElseThrow(()-> new BatchNotFoundException("Batch not found"));

        return ResponseEntity.ok(batchService.getBatchFullProfileByBatch(batch));

    }

    // Remove Course from Batch
    @DeleteMapping("/batches/profile/{batch-id}/courses/{course-id}")
    public ResponseEntity<?> removeCourseFromBatch(@PathVariable("batch-id") long batchId, @PathVariable("course-id") long courseId) throws BatchNotFoundException, CourseNotFoundException {
        Batch batch = batchService.getBatchById(batchId)
                .orElseThrow(()-> new BatchNotFoundException("Batch not found"));
        Course course = courseService.getCourseByCourseId(courseId);
        batchService.removeCourseFromBatch(batch,course);
        return ResponseEntity.ok("Course Removed Successfully");
    }

    // Remove trainers from Batch
    @DeleteMapping("/batches/profile/{batch-id}/trainers/{trainer-id}")
    public ResponseEntity<?> removeTrainersFromBatch(@PathVariable("batch-id") long batchId, @PathVariable("trainer-id") long trainerId) throws BatchNotFoundException, CourseNotFoundException, TrainerNotFoundException {
        Batch batch = batchService.getBatchById(batchId)
                .orElseThrow(()-> new BatchNotFoundException("Batch not found"));
        Trainer trainer = trainerRepository.findById(trainerId)
                        .orElseThrow(()-> new TrainerNotFoundException("Trainer Account Not found"));
        batchService.removeTrainerFromBatch(batch,trainer);
        return ResponseEntity.ok("Trainer Removed Successfully");
    }

    // Remove trainees from Batch
    @DeleteMapping("/batches/profile/{batch-id}/trainees/{trainee-id}")
    public ResponseEntity<?> removeTraineesFromBatch(@PathVariable("batch-id") long batchId, @PathVariable("trainee-id") long traineeId) throws BatchNotFoundException, CourseNotFoundException, TrainerNotFoundException, TraineeNotFoundException {
        Batch batch = batchService.getBatchById(batchId)
                .orElseThrow(()-> new BatchNotFoundException("Batch not found"));
        Trainee trainee = traineeRepository.findById(traineeId)
                        .orElseThrow(()-> new TraineeNotFoundException("Trainee Account not found"));
        batchService.removeTraineeFromBatch(batch,trainee);
        return ResponseEntity.ok("Trainee Removed Successfully");
    }
    @DeleteMapping("/batches/{batch-id}")
    public ResponseEntity<MessageResponse> deleteBatch(@PathVariable("batch-id") long batchId) throws BatchNotFoundException {
        batchService.deleteBatchById(batchId);
        return ResponseEntity.ok(new MessageResponse("Batch remove successfully"));
    }

    @PostMapping("/batches/assign-user")
    public ResponseEntity<MessageResponse> assign(@RequestBody UserAssignRequestDto request) throws UserNotFoundException, BatchNotFoundException {
        batchService.assignUserToBatch(request);
        return ResponseEntity.ok(new MessageResponse("Assigned Success"));
    }

    @GetMapping("/assign/batches/{batch-id}/trainee/{trainee-id}")
    public ResponseEntity<MessageResponse> traineeAssign(@PathVariable("batch-id") long batchId, @PathVariable("trainee-id") long traineeId) throws BatchNotFoundException, TraineeNotFoundException {

        Batch batch = batchService.getBatchById(batchId)
                .orElseThrow(()-> new BatchNotFoundException("Batch not found"));
        Trainee trainee = userService.getTraineeById(traineeId)
                .orElseThrow(()-> new TraineeNotFoundException("Trainee Profile Not found"));

        batchService.assignTraineeToBatch(batch, trainee);
        return ResponseEntity.ok(new MessageResponse("Assigned Success"));
    }

    @GetMapping("/assign/batches/{batch-id}/trainer/{trainer-id}")
    public ResponseEntity<MessageResponse> trainerAssign(@PathVariable("batch-id") long batchId, @PathVariable("trainer-id") long trainerId) throws BatchNotFoundException, TrainerNotFoundException {

        Batch batch = batchService.getBatchById(batchId)
                .orElseThrow(()-> new BatchNotFoundException("Batch not found"));
        Trainer trainer = userService.getTrainerById(trainerId)
                .orElseThrow(()-> new TrainerNotFoundException("Trainer Profile Not found"));

        batchService.assignTrainerToBatch(batch, trainer);
        return ResponseEntity.ok(new MessageResponse("Assigned Success"));
    }




}
