package com.training.erp.service;

import com.training.erp.model.request.*;
import com.training.erp.model.response.BatchDetailsResponse;
import com.training.erp.model.response.BatchCreateResponse;
import com.training.erp.model.response.MessageResponse;

import java.util.List;

public interface BatchService {
    BatchCreateResponse save(BatchCreateRequest request);
    BatchCreateResponse update(BatchUpdateRequest request);
    List<BatchCreateResponse> getAllBatch();
    MessageResponse assignUserToBatch(AddUserToBatchRequest request);
    MessageResponse removeUserFromBatch(RemoveUserRequest request);

    MessageResponse addCourseToBatch(AddCourseToBatchRequest request);
    MessageResponse removeCourseFromBatch(RemoveCourseRequest request);

    BatchDetailsResponse getBatchById(long id);
    boolean existsByBatchName(String name);
    void deleteBatchById(long id);

}
