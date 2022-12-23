package com.training.erp.service;

import com.training.erp.model.request.AssignmentCreateRequest;
import com.training.erp.model.request.AssignmentEvaluateRequest;
import com.training.erp.model.response.AssignmentResponse;
import com.training.erp.model.response.AssignmentSubmissionResponse;
import com.training.erp.model.response.SubmissionResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssignmentService {
    AssignmentResponse save(AssignmentCreateRequest request);
    List<AssignmentResponse> getAssignments();
    List<AssignmentResponse> getAssignmentsByCourse(long courseId);
    AssignmentResponse getAssignmentById(long assignmentId);
    void deleteAssignmentById(long id);
    List<AssignmentSubmissionResponse> getAssignmentSubmissionByAssignmentId(long assignmentId);
    AssignmentSubmissionResponse getTraineesSubmissionBySubmissionId(long submissionId);
    void updateSubmission(AssignmentEvaluateRequest request);

    boolean submitAssignment(MultipartFile file, long aid, long sid);

    List<SubmissionResponse> getSubmissionsByStudentUsername(String username);
}
