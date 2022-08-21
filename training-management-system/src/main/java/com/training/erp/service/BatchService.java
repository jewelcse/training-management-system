package com.training.erp.service;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.entity.Trainee;
import com.training.erp.entity.Trainer;
import com.training.erp.exception.BatchNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.BatchRequestDto;
import com.training.erp.model.request.UserAssignRequestDto;
import com.training.erp.model.response.BatchFullProfileResponse;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BatchService {
    void createNewBatch(BatchRequestDto request);
    List<Batch> getAllBatch();
    void assignUserToBatch(UserAssignRequestDto request) throws BatchNotFoundException, UserNotFoundException;
    Optional<Batch> getBatchById(long batchId) throws BatchNotFoundException;
    Batch getBatchByTrainee(Trainee trainee) throws BatchNotFoundException;
    List<Batch> getBatchesByTrainerId(Long trainerId);
    boolean existsByBatchName(String batch_name);
    void deleteBatchById(long batchId);
    boolean existsById(long batchId);
    void assignTraineeToBatch(Batch batch, Trainee trainee) throws BatchNotFoundException;
    void assignTrainerToBatch(Batch batch, Trainer trainer);
    BatchFullProfileResponse getBatchFullProfileByBatch(Batch batch);
    void updateBatch(Batch batch);
    void removeCourseFromBatch(Batch batch, Course course);
    void removeTrainerFromBatch(Batch batch, Trainer trainer);
    void removeTraineeFromBatch(Batch batch, Trainee trainee);
}
