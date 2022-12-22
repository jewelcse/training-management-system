package com.training.erp.service;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.model.request.BatchRequestDto;
import com.training.erp.model.request.AssignUserRequest;
import com.training.erp.model.request.RemoveUserRequest;
import com.training.erp.model.response.BatchDetails;
import com.training.erp.model.response.BatchFullProfileResponse;
import com.training.erp.model.response.BatchResponse;
import com.training.erp.model.response.MessageResponse;

import java.util.List;

public interface BatchService {
    BatchResponse save(BatchRequestDto request);
    List<BatchResponse> getAllBatch();
    MessageResponse assignUserToBatch(AssignUserRequest request);
    MessageResponse removeUserFromBatch(RemoveUserRequest request);
    BatchDetails getBatchById(long id);
    boolean existsByBatchName(String batch_name);
    void deleteBatchById(long batchId);

}
