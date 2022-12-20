package com.training.erp.serviceImpl;

import com.training.erp.entity.*;
import com.training.erp.exception.*;
import com.training.erp.model.request.AssignmentRequestDto;
import com.training.erp.model.request.AssignmentSubmissionUpdateRequest;
import com.training.erp.repository.*;
import com.training.erp.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class AssignmentServiceImpl implements AssignmentService {
    @Autowired
    private AssignmentRepository assignmentRepository;
    @Autowired
    private AssignmentSubmissionRepository traineesAssignmentSubmissionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CourseRepository courseRepository;


    @Override
    public void createAssignment(AssignmentRequestDto assignmentRequestDto, Principal principal) throws UserNotFoundException, CourseNotFoundException {

        User user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Course course = courseRepository.findById(assignmentRequestDto.getCourse_id())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        Assignment assignment = new Assignment();
        assignment.setTitle(assignmentRequestDto.getTitle());
        assignment.setTotalMarks(assignmentRequestDto.getMarks());
        assignment.setFileLocation(assignmentRequestDto.getFile_path());
        assignment.setCourse(course);
        assignmentRepository.save(assignment);
    }

    @Override
    public List<Assignment> getAssignments() {
        return assignmentRepository.findAll();
    }

    @Override
    public List<Assignment> getAssignments(Principal principal) {
        User user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        return null;
    }

    @Override
    public List<Assignment> getAssignmentsByCourse(long courseId) throws CourseNotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        return assignmentRepository.findAllByCourse(course);
    }

    @Override
    public Assignment getAssignmentByAssignmentId(long assignmentId) throws AssignmentNotFoundException {
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
    }

    @Override
    public void deleteAssignmentByAssignmentId(long assignmentId) throws AssignmentNotFoundException {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
        assignmentRepository.deleteById(assignment.getId());
    }

    @Override
    public List<AssignmentSubmission> getAssignmentSubmissionByAssignmentId(long assignmentId) throws AssignmentNotFoundException {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
        return traineesAssignmentSubmissionRepository.findAllByAssignment(assignment);
    }

    @Override
    public AssignmentSubmission getTraineesSubmissionBySubmissionId(long submissionId) throws TraineesAssignmentSubmissionNotFoundException {
        return traineesAssignmentSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new TraineesAssignmentSubmissionNotFoundException("Submission not found"));
    }

    @Override
    public void updateSubmission(AssignmentSubmissionUpdateRequest submission) throws TraineesAssignmentSubmissionNotFoundException {
        AssignmentSubmission traineesAssignmentSubmissions
                = traineesAssignmentSubmissionRepository.findById(submission.getSubmissionId())
                .orElseThrow(() -> new TraineesAssignmentSubmissionNotFoundException("Submission not found"));
        traineesAssignmentSubmissions.setObtainedMarks(submission.getMarks());
        traineesAssignmentSubmissionRepository.save(traineesAssignmentSubmissions);
    }

}
