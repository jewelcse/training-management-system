package com.training.erp.service;

import com.training.erp.entity.Assignment;
import com.training.erp.entity.Course;
import com.training.erp.entity.TraineesAssignmentSubmission;
import com.training.erp.exception.*;
import com.training.erp.model.request.AssignmentRequestDto;
import com.training.erp.model.request.AssignmentSubmissionUpdateRequest;

import java.security.Principal;
import java.util.List;

public interface AssignmentService {
    void createAssignment(AssignmentRequestDto assignmentRequestDto, Principal principal) throws BatchNotFoundException, UserNotFoundException, CourseNotFoundException;
    List<Assignment> getAssignments();
    List<Assignment> getAssignments(Principal principal);
    List<Assignment> getAssignmentsByCourse(long courseId) throws CourseNotFoundException;
    Assignment getAssignmentByAssignmentId(long assignmentId) throws AssignmentNotFoundException;
    void deleteAssignmentByAssignmentId(long assignmentId) throws AssignmentNotFoundException;
    List<TraineesAssignmentSubmission> getAssignmentSubmissionByAssignmentId(long assignmentId) throws AssignmentNotFoundException;
    TraineesAssignmentSubmission getTraineesSubmissionBySubmissionId(long submissionId) throws TraineesAssignmentSubmissionNotFoundException;
    void updateSubmission(AssignmentSubmissionUpdateRequest submission) throws TraineesAssignmentSubmissionNotFoundException;
}
