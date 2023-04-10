package com.tms.service;

import com.tms.dto.request.*;
import com.tms.dto.response.*;
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
