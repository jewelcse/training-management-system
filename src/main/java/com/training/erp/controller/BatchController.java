package com.training.erp.controller;

import com.training.erp.model.request.*;
import com.training.erp.model.response.BatchDetails;
import com.training.erp.model.response.BatchResponse;
import com.training.erp.model.response.MessageResponse;
import com.training.erp.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BatchController {
    private final BatchService batchService;

    @PostMapping("/batches")
    public ResponseEntity<BatchResponse> create(@RequestBody BatchCreateRequest request) {
        return ResponseEntity.ok(batchService.save(request));
    }
    @PutMapping("/batches")
    public ResponseEntity<BatchResponse> update(@RequestBody BatchUpdateRequest request) {
        return ResponseEntity.ok(batchService.update(request));
    }

    @GetMapping("/batches")
    public ResponseEntity<List<BatchResponse>> getBatches() {
        return ResponseEntity.ok(batchService.getAllBatch());
    }

    @GetMapping("/batches/{id}")
    public ResponseEntity<BatchDetails> getBatch(@PathVariable("id") long id) {
        return ResponseEntity.ok(batchService.getBatchById(id));
    }
    @DeleteMapping("/batches/{id}")
    public ResponseEntity<MessageResponse> removeBatch(@PathVariable("id") long id) {
        batchService.deleteBatchById(id);
        return ResponseEntity.ok(new MessageResponse("Batch remove successfully"));
    }

    @PostMapping("/batches/add-user")
    public ResponseEntity<MessageResponse> addUser(@RequestBody AssignUserRequest request) {
        return new ResponseEntity<>(batchService.assignUserToBatch(request), HttpStatus.ACCEPTED);
    }

    @PostMapping("/batches/remove-user")
    public ResponseEntity<MessageResponse> removeUser(@RequestBody RemoveUserRequest request) {
        return new ResponseEntity<>(batchService.removeUserFromBatch(request), HttpStatus.ACCEPTED);
    }

    @PostMapping("/batches/add-course")
    public ResponseEntity<MessageResponse> addCourse(@RequestBody AddCourseRequest request){
        return new ResponseEntity<>(batchService.addCourseToBatch(request), HttpStatus.ACCEPTED);
    }

    @PostMapping("/batches/remove-course")
    public ResponseEntity<MessageResponse> removeCourse(@RequestBody RemoveCourseRequest request){
        return new ResponseEntity<>(batchService.removeCourseFromBatch(request), HttpStatus.ACCEPTED);
    }

}
