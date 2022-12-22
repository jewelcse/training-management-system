package com.training.erp.serviceImpl;

import com.training.erp.entity.*;
import com.training.erp.exception.*;
import com.training.erp.model.request.AssignmentRequestDto;
import com.training.erp.model.request.AssignmentSubmissionUpdateRequest;
import com.training.erp.model.response.AssignmentResponse;
import com.training.erp.repository.*;
import com.training.erp.service.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private BatchRepository batchRepository;


    @Override
    public AssignmentResponse save(AssignmentRequestDto request, Principal principal){

        Optional<User> user = userRepository
                .findByUsername(principal.getName());
        if (user.isEmpty()) {
            throw new UserNotFoundException("USER NOT FOUND");
        }
        Optional<Batch> batch = batchRepository.findById(request.getBatchId());
        if (batch.isEmpty()) {
            throw new BatchNotFoundException("BATCH NOT FOUND");
        }

        Optional<Course> course = courseRepository.findById(request.getCourseId());
        if (course.isEmpty()) {
            throw new CourseNotFoundException("COURSE NOT FOUND");
        }


        Assignment assignment = Assignment.builder()
                .title(request.getTitle())
                .fileLocation(request.getFilePath())
                .course(course.get())
                .totalMarks(request.getMarks())
                .build();
        assignmentRepository.save(assignment);

        return AssignmentResponse.builder()
                .title(request.getTitle())
                .marks(request.getMarks())
                .filePath(request.getFilePath())
                .batchName(batch.get().getBatchName())
                .courseName(course.get().getCourseName())
                .build();
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
    public List<Assignment> getAssignmentsByCourse(long courseId){
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        return assignmentRepository.findAllByCourse(course);
    }

    @Override
    public Assignment getAssignmentByAssignmentId(long assignmentId){
        return assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
    }

    @Override
    public void deleteAssignmentByAssignmentId(long assignmentId){
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
        assignmentRepository.deleteById(assignment.getId());
    }

    @Override
    public List<AssignmentSubmission> getAssignmentSubmissionByAssignmentId(long assignmentId){
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
        return traineesAssignmentSubmissionRepository.findAllByAssignment(assignment);
    }

    @Override
    public AssignmentSubmission getTraineesSubmissionBySubmissionId(long submissionId) {
        return traineesAssignmentSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new TraineesAssignmentSubmissionNotFoundException("Submission not found"));
    }

    @Override
    public void updateSubmission(AssignmentSubmissionUpdateRequest submission){
        AssignmentSubmission traineesAssignmentSubmissions
                = traineesAssignmentSubmissionRepository.findById(submission.getSubmissionId())
                .orElseThrow(() -> new TraineesAssignmentSubmissionNotFoundException("Submission not found"));
        traineesAssignmentSubmissions.setObtainedMarks(submission.getMarks());
        traineesAssignmentSubmissionRepository.save(traineesAssignmentSubmissions);
    }

}
