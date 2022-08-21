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
    private TraineesAssignmentSubmissionRepository traineesAssignmentSubmissionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private TrainerRepository trainerRepository;

    @Override
    public void createAssignment(AssignmentRequestDto assignmentRequestDto, Principal principal) throws UserNotFoundException, CourseNotFoundException {

        User user = userRepository
                .findByUsername(principal.getName())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Course course = courseRepository.findById(assignmentRequestDto.getCourse_id())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        Trainer trainer = course.getTrainer();
        Assignment assignment = new Assignment();
        assignment.setTitle(assignmentRequestDto.getTitle());
        assignment.setMarks(assignmentRequestDto.getMarks());
        assignment.setFilePath(assignmentRequestDto.getFile_path());
        assignment.setTrainer(trainer);
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
        Trainer trainer = trainerRepository.findByUser(user);

        return assignmentRepository.findAllAssignmentsByUserId(trainer.getId());
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
    public List<TraineesAssignmentSubmission> getAssignmentSubmissionByAssignmentId(long assignmentId) throws AssignmentNotFoundException {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
        return traineesAssignmentSubmissionRepository.findAllByAssignment(assignment);
    }

    @Override
    public TraineesAssignmentSubmission getTraineesSubmissionBySubmissionId(long submissionId) throws TraineesAssignmentSubmissionNotFoundException {
        return traineesAssignmentSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new TraineesAssignmentSubmissionNotFoundException("Submission not found"));
    }

    @Override
    public void updateSubmission(AssignmentSubmissionUpdateRequest submission) throws TraineesAssignmentSubmissionNotFoundException {
        TraineesAssignmentSubmission traineesAssignmentSubmissions
                = traineesAssignmentSubmissionRepository.findById(submission.getSubmissionId())
                .orElseThrow(() -> new TraineesAssignmentSubmissionNotFoundException("Submission not found"));
        traineesAssignmentSubmissions.setObtainedMarks(submission.getMarks());
        traineesAssignmentSubmissionRepository.save(traineesAssignmentSubmissions);
    }

}
