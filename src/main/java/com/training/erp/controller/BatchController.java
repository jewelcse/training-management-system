package com.training.erp.controller;

import com.training.erp.model.request.BatchRequestDto;
import com.training.erp.model.request.AssignUserRequest;
import com.training.erp.model.request.RemoveUserRequest;
import com.training.erp.model.response.BatchDetails;
import com.training.erp.model.response.BatchResponse;
import com.training.erp.model.response.MessageResponse;
import com.training.erp.service.BatchService;
import com.training.erp.service.CourseService;
import com.training.erp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<MessageResponse> create(@RequestBody BatchRequestDto request) {
        if (batchService.existsByBatchName(request.getBatchName())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(request.getBatchName() + " ALREADY EXIST!"));
        }
        batchService.save(request);
        return ResponseEntity.ok(new MessageResponse("BATCH CREATED SUCCESS!"));
    }

    @GetMapping("/batches")
    public ResponseEntity<List<BatchResponse>> getBatches() {
        return ResponseEntity.ok(batchService.getAllBatch());
    }

    @GetMapping("/batches/{id}")
    public ResponseEntity<BatchDetails> getBatch(@PathVariable("id") long id) {
        return ResponseEntity.ok(batchService.getBatchById(id));
    }

    // Remove Course from Batch
    @DeleteMapping("/batches/{bid}/courses/{cid}")
    public ResponseEntity<?> removeCourseFromBatch(@PathVariable("batch-id") long batchId, @PathVariable("course-id") long courseId) {
        //Batch batch = batchService.getBatchById(batchId);
        //Course course = courseService.getCourseById(courseId);
        //batchService.removeCourseFromBatch(batch,course);
        return ResponseEntity.ok("Course Removed Successfully");
    }




    @DeleteMapping("/batches/{id}")
    public ResponseEntity<MessageResponse> removeBatch(@PathVariable("id") long id) {
        batchService.deleteBatchById(id);
        return ResponseEntity.ok(new MessageResponse("Batch remove successfully"));
    }

    @PostMapping("/batches/assign-user")
    public ResponseEntity<MessageResponse> assignUser(@RequestBody AssignUserRequest request) {
        return new ResponseEntity<>(batchService.assignUserToBatch(request), HttpStatus.ACCEPTED);
    }

    @PostMapping("/batches/remove-user")
    public ResponseEntity<MessageResponse> removeUser(@RequestBody RemoveUserRequest request){
        return new ResponseEntity<>(batchService.removeUserFromBatch(request),HttpStatus.ACCEPTED);
    }
}
