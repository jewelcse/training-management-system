package com.training.erp.service;

import com.training.erp.entity.Assignment;
import com.training.erp.entity.AssignmentSubmission;
import com.training.erp.model.request.AssignmentRequestDto;
import com.training.erp.model.request.AssignmentSubmissionUpdateRequest;
import com.training.erp.model.response.AssignmentResponse;

import java.security.Principal;
import java.util.List;

public interface AssignmentService {
    AssignmentResponse save(AssignmentRequestDto assignmentRequestDto, Principal principal);
    List<Assignment> getAssignments();
    List<Assignment> getAssignments(Principal principal);
    List<Assignment> getAssignmentsByCourse(long courseId);
    Assignment getAssignmentByAssignmentId(long assignmentId);
    void deleteAssignmentByAssignmentId(long assignmentId);
    List<AssignmentSubmission> getAssignmentSubmissionByAssignmentId(long assignmentId);
    AssignmentSubmission getTraineesSubmissionBySubmissionId(long submissionId);
    void updateSubmission(AssignmentSubmissionUpdateRequest submission);
}
