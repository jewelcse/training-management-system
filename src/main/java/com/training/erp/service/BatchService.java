package com.training.erp.service;

import com.training.erp.entity.Batch;
import com.training.erp.model.request.*;
import com.training.erp.model.response.BatchDetails;
import com.training.erp.model.response.BatchResponse;
import com.training.erp.model.response.MessageResponse;

import java.util.List;
import java.util.Optional;

public interface BatchService {
    BatchResponse save(BatchCreateRequest request);
    BatchResponse update(BatchUpdateRequest request);
    List<BatchResponse> getAllBatch();
    MessageResponse assignUserToBatch(AssignUserRequest request);
    MessageResponse removeUserFromBatch(RemoveUserRequest request);

    MessageResponse addCourseToBatch(AddCourseRequest request);
    MessageResponse removeCourseFromBatch(RemoveCourseRequest request);

    BatchDetails getBatchById(long id);
    boolean existsByBatchName(String name);
    void deleteBatchById(long id);

}
