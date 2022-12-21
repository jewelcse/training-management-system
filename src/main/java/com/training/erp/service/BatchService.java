package com.training.erp.service;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.exception.BatchNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.BatchRequestDto;
import com.training.erp.model.request.UserAssignRequestDto;
import com.training.erp.model.response.BatchFullProfileResponse;
import com.training.erp.model.response.BatchResponseDto;

import java.util.List;
import java.util.Optional;

public interface BatchService {
    BatchResponseDto save(BatchRequestDto request);
    List<Batch> getAllBatch();
    void assignUserToBatch(UserAssignRequestDto request) throws BatchNotFoundException, UserNotFoundException;
    Optional<Batch> getBatchById(long batchId) throws BatchNotFoundException;
    boolean existsByBatchName(String batch_name);
    void deleteBatchById(long batchId);
    BatchFullProfileResponse getBatchFullProfileByBatch(Batch batch);
    void updateBatch(Batch batch);
    void removeCourseFromBatch(Batch batch, Course course);
}
