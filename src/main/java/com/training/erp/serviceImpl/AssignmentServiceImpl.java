package com.training.erp.serviceImpl;

import com.training.erp.entity.*;
import com.training.erp.exception.*;
import com.training.erp.mapper.AssignmentMapper;
import com.training.erp.mapper.UserMapper;
import com.training.erp.model.request.AssignmentCreateRequest;
import com.training.erp.model.request.AssignmentEvaluateRequest;
import com.training.erp.model.response.*;
import com.training.erp.repository.*;
import com.training.erp.service.AssignmentService;
import com.training.erp.util.FilesStorageService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;

    private final AssignmentSubmissionRepository traineesAssignmentSubmissionRepository;

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    private final AssignmentMapper assignmentMapper;

    private final UserMapper userMapper;

    private final FilesStorageService filesStorageService;

    @Override
    public AssignmentResponse save(AssignmentCreateRequest request) {

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
        Assignment response = assignmentRepository.save(assignment);

        return assignmentMapper.assignmentToAssignmentResponse(response);
    }

    @Override
    public List<AssignmentResponse> getAssignments() {
        return assignmentMapper.assignmentsToAssignmentsResponse(assignmentRepository.findAll());
    }

    @Override
    public List<AssignmentResponse> getAssignmentsByCourse(long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        return assignmentMapper.assignmentsToAssignmentsResponse(assignmentRepository.findAllByCourse(course));
    }

    @Override
    public AssignmentResponse getAssignmentById(long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
        return assignmentMapper.assignmentToAssignmentResponse(assignment);
    }

    @Override
    public void deleteAssignmentById(long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));
        assignmentRepository.deleteById(assignment.getId());
    }

    @Override
    public List<AssignmentSubmissionResponse> getSubmissionsByAssignmentId(long assignmentId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new AssignmentNotFoundException("Assignment not found"));

        List<AssignmentSubmission> submissions = traineesAssignmentSubmissionRepository.findAllByAssignment(assignment);

        List<AssignmentSubmissionResponse> responses = new ArrayList<>();

        submissions.forEach(submission -> {
            responses.add(AssignmentSubmissionResponse.builder()
                    .assignmentTitle(submission.getAssignment().getTitle())
                    .totalMarks(submission.getAssignment().getTotalMarks())
                    .submissionId(submission.getId())
                    .obtainedMarks(submission.getObtainedMarks())
                    .student(userMapper.profileToUserProfile(submission.getStudent().getProfile()))
                    .fileLocation(submission.getFileLocation())
                    .build());
        });

        return responses;
    }

    @Override
    public AssignmentSubmissionResponse getSubmissionById(long id) {
        AssignmentSubmission submission = traineesAssignmentSubmissionRepository.findById(id)
                .orElseThrow(() -> new TraineesAssignmentSubmissionNotFoundException("Submission not found"));


        UserProfile profile = UserProfile.builder()
                .firstName(submission.getStudent().getProfile().getFirstName())
                .lastName(submission.getStudent().getProfile().getLastName())
                .username(submission.getStudent().getUsername())
                .address1(submission.getStudent().getProfile().getAddress1())
                .address2(submission.getStudent().getProfile().getAddress2())
                .city(submission.getStudent().getProfile().getCity())
                .street(submission.getStudent().getProfile().getStreet())
                .city(submission.getStudent().getProfile().getCity())
                .dateOfBirth(submission.getStudent().getProfile().getDateOfBirth())
                .gender(submission.getStudent().getProfile().getGender())
                .country(submission.getStudent().getProfile().getCountry())
                .zipCode(submission.getStudent().getProfile().getZipCode())
                .state(submission.getStudent().getProfile().getState())
                .phoneNumber(submission.getStudent().getProfile().getPhoneNumber())
                .build();

        return AssignmentSubmissionResponse.builder()
                .submissionId(submission.getId())
                .assignmentTitle(submission.getAssignment().getTitle())
                .totalMarks(submission.getAssignment().getTotalMarks())
                .obtainedMarks(submission.getObtainedMarks())
                .fileLocation(submission.getFileLocation())
                .student(profile)
                .build();
    }

    @Override
    public UpdatedSubmissionResponse updateSubmission(AssignmentEvaluateRequest request) {
        AssignmentSubmission evaluatedAssignment
                = traineesAssignmentSubmissionRepository.findById(request.getSubmissionId())
                .orElseThrow(() -> new TraineesAssignmentSubmissionNotFoundException("Submission not found"));
        evaluatedAssignment.setObtainedMarks(request.getMarks());
        AssignmentSubmission submission = traineesAssignmentSubmissionRepository.save(evaluatedAssignment);
        return UpdatedSubmissionResponse.builder()
                .submissionId(submission.getId())
                .totalMarks(submission.getAssignment().getTotalMarks())
                .obtainedMarks(submission.getObtainedMarks())
                .build();
    }

    @Override
    public boolean submitAssignment(MultipartFile file, long aid, long sid) {

        Optional<User> student = userRepository.findById(sid);
        if (student.isEmpty()) {
            throw new UserNotFoundException("USER NOT FOUND!");
        }

        System.out.println("user in service: " + student.get().getId());

        Optional<Assignment> assignment = assignmentRepository.findById(aid);
        if (assignment.isEmpty()) {
            throw new AssignmentNotFoundException("ASSIGNMENT NOT FOUND!");
        }
        System.out.println("assignment in service: " + assignment.get().getId());

        boolean doesSubmitted
                = traineesAssignmentSubmissionRepository.existsByStudentAndAssignment(student.get(), assignment.get());

        System.out.println("submitted: " + doesSubmitted);


        if (!doesSubmitted) {
            AssignmentSubmission submission = new AssignmentSubmission();

            // Save the pdf/ image/ docs file to upload folder
            String filePath = filesStorageService.saveFile(file);
            submission.setFileLocation("http://localhost:8090/api/v1/assignments/trainees/submissions/file/" + filePath);
            submission.setObtainedMarks(0);
            submission.setAssignment(assignment.get());
            submission.setStudent(student.get());
            traineesAssignmentSubmissionRepository.save(submission);
            return true;
        }
        return false;
    }

    @Override
    public List<SubmissionResponse> getSubmissionsByStudent(String username) {

        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            throw new UserNotFoundException("USER NOT FOUND!");
        }

        List<AssignmentSubmission> submissions = traineesAssignmentSubmissionRepository.findAllByStudent(user.get());
        List<SubmissionResponse> responses = new ArrayList<>();

        System.out.println("submission size " + submissions.size());
        submissions.forEach(submission -> responses.add(SubmissionResponse.builder()

                .assignmentTitle(submission.getAssignment().getTitle())
                .courseName(submission.getAssignment().getCourse().getCourseName())
                .submissionId(submission.getId())
                .fileLocation(submission.getFileLocation())
                .obtainedMarks(submission.getObtainedMarks())
                .totalMarks(submission.getAssignment().getTotalMarks())
                .build()));

        return responses;
    }

}
