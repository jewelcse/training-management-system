package com.training.erp.serviceImpl;

import com.training.erp.entity.assignments.Assignment;
import com.training.erp.entity.assignments.AssignmentSubmission;
import com.training.erp.entity.courses.Course;
import com.training.erp.entity.users.Profile;
import com.training.erp.entity.users.User;
import com.training.erp.exception.AssignmentNotFoundException;
import com.training.erp.exception.CourseNotFoundException;
import com.training.erp.mapper.AssignmentMapper;
import com.training.erp.mapper.UserMapper;
import com.training.erp.model.request.AssignmentCreateRequest;
import com.training.erp.model.response.AssignmentResponse;
import com.training.erp.model.response.AssignmentSubmissionResponse;
import com.training.erp.model.response.UserProfile;
import com.training.erp.repository.AssignmentRepository;
import com.training.erp.repository.AssignmentSubmissionRepository;
import com.training.erp.repository.CourseRepository;
import com.training.erp.repository.UserRepository;
import com.training.erp.service.AssignmentService;
import com.training.erp.util.FilesStorageService;
import lombok.AllArgsConstructor;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AssignmentServiceImplTest {

    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private AssignmentSubmissionRepository traineesAssignmentSubmissionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;

    @Mock
    private AssignmentMapper assignmentMapper;
    @Mock
    private UserMapper userMapper;

    private FilesStorageService filesStorageService;

    @InjectMocks
    private AssignmentServiceImpl assignmentService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        assignmentService = new AssignmentServiceImpl(assignmentRepository,
                traineesAssignmentSubmissionRepository,
                userRepository,
                courseRepository,
                assignmentMapper,
                userMapper,
                filesStorageService);

    }

    @Test
    void save_ValidRequest_shouldAssignmentSuccessfullySaved() {
        // Arrange
        String courseName = "course01";
        String courseDescription = "course description";
        long courseId = 1l;
        long assignmentId = 1l;
        double marks = 50.0;
        String assignmentTitle = "first assignment";
        String assignmentFileLocation = "c://files/assignmet01.pdf";

        AssignmentCreateRequest request = AssignmentCreateRequest.builder()
                .courseId(courseId)
                .title(assignmentTitle)
                .marks(marks)
                .filePath(assignmentFileLocation)
                .build();
        Course course = Course.builder()
                .id(courseId)
                .courseName(courseName)
                .courseDescription(courseDescription)
                .build();
        Assignment assignment = Assignment.builder()
                .title(assignmentTitle)
                .fileLocation(assignmentFileLocation)
                .course(course)
                .totalMarks(marks)
                .build();
        AssignmentResponse expectedResponse = AssignmentResponse.builder()
                .id(assignmentId)
                .title(assignmentTitle)
                .marks(marks)
                .filePath(assignmentFileLocation)
                .courseName(courseName)
                .build();

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(assignmentRepository.save(any(Assignment.class))).thenReturn(assignment);
        when(assignmentMapper.assignmentToAssignmentResponse(assignment)).thenReturn(expectedResponse);
        // Act

        AssignmentResponse response = assignmentService.save(request);

        // Assert
        assertNotNull(response);
        assertEquals(expectedResponse.getId(), response.getId());
        assertEquals(expectedResponse.getTitle(), response.getTitle());
        assertEquals(expectedResponse.getFilePath(), response.getFilePath());
        assertEquals(expectedResponse.getCourseName(), response.getCourseName());
        assertEquals(expectedResponse.getMarks(), response.getMarks());

        verify(courseRepository, times(1)).findById(1L);
        verify(assignmentRepository, times(1)).save(any(Assignment.class));
        verify(assignmentMapper, times(1)).assignmentToAssignmentResponse(assignment);

    }

    @Test
    void save_InValidRequest_shouldReturnCourseNotFound() {
        // Arrange
        long courseId = 1L;
        double marks = 50.0;
        String assignmentTitle = "first assignment";
        String assignmentFileLocation = "c://files/assignmet01.pdf";

        AssignmentCreateRequest request = AssignmentCreateRequest.builder()
                .courseId(courseId)
                .title(assignmentTitle)
                .marks(marks)
                .filePath(assignmentFileLocation)
                .build();

        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(CourseNotFoundException.class, () -> {
            assignmentService.save(request);
        });

    }

    @Test
    void getAssignments_ValidRequest_shouldReturnTheListOfAssignments() {
        // arrange
        String courseName = "course01";
        String courseDescription = "course description";
        long courseId = 1l;
        long assignmentId = 1l;
        double marks = 50.0;
        String assignmentTitle = "first assignment";
        String assignmentFileLocation = "c://files/assignmet01.pdf";

        Course course = Course.builder()
                .id(courseId)
                .courseName(courseName)
                .courseDescription(courseDescription)
                .build();

        Assignment assignment = Assignment.builder()
                .title(assignmentTitle)
                .fileLocation(assignmentFileLocation)
                .course(course)
                .totalMarks(marks)
                .build();

        AssignmentResponse assignmentResponse = AssignmentResponse.builder()
                .id(assignmentId)
                .title(assignmentTitle)
                .marks(marks)
                .filePath(assignmentFileLocation)
                .courseName(courseName)
                .build();

        List<Assignment> assignments = List.of(assignment, assignment, assignment);
        List<AssignmentResponse> expectedResponse = List.of(assignmentResponse, assignmentResponse, assignmentResponse);

        when(assignmentRepository.findAll()).thenReturn(assignments);
        when(assignmentMapper.assignmentsToAssignmentsResponse(assignments)).thenReturn(expectedResponse);

        // act
        List<AssignmentResponse> response = assignmentService.getAssignments();

        assertEquals(response.size(), expectedResponse.size());

    }

    @Test
    void getAssignmentsByCourse_ValidRequest_ShouldReturnTheListOfAssignmentByCourseId() {
        // arrange
        String courseName = "course01";
        String courseDescription = "course description";
        long courseId = 1L;
        long assignmentId = 1L;
        double marks = 50.0;
        String assignmentTitle = "first assignment";
        String assignmentFileLocation = "c://files/assignmet01.pdf";

        Course course = Course.builder()
                .id(courseId)
                .courseName(courseName)
                .courseDescription(courseDescription)
                .build();

        Assignment assignment = Assignment.builder()
                .title(assignmentTitle)
                .fileLocation(assignmentFileLocation)
                .course(course)
                .totalMarks(marks)
                .build();

        AssignmentResponse assignmentResponse = AssignmentResponse.builder()
                .id(assignmentId)
                .title(assignmentTitle)
                .marks(marks)
                .filePath(assignmentFileLocation)
                .courseName(courseName)
                .build();

        List<Assignment> assignments = List.of(assignment);
        List<AssignmentResponse> expectedResponse = List.of(assignmentResponse);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(assignmentRepository.findAllByCourse(course)).thenReturn(assignments);
        when(assignmentMapper.assignmentsToAssignmentsResponse(assignments)).thenReturn(expectedResponse);

        // act
        List<AssignmentResponse> responses = assignmentService.getAssignmentsByCourse(courseId);

        // assert
        assertEquals(expectedResponse.size(), responses.size());

    }

    @Test
    void getAssignmentById_ValidRequest_ShouldReturnAssignmentByAssignmentId() {

        // arrange
        String courseName = "course01";
        String courseDescription = "course description";
        long courseId = 1L;
        long assignmentId = 1L;
        double marks = 50.0;
        String assignmentTitle = "first assignment";
        String assignmentFileLocation = "c://files/assignmet01.pdf";

        Course course = Course.builder()
                .id(courseId)
                .courseName(courseName)
                .courseDescription(courseDescription)
                .build();
        Assignment assignment = Assignment.builder()
                .title(assignmentTitle)
                .fileLocation(assignmentFileLocation)
                .course(course)
                .totalMarks(marks)
                .build();

        AssignmentResponse expectedResponse = AssignmentResponse.builder()
                .id(assignmentId)
                .title(assignmentTitle)
                .marks(marks)
                .filePath(assignmentFileLocation)
                .courseName(courseName)
                .build();

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
        when(assignmentMapper.assignmentToAssignmentResponse(assignment)).thenReturn(expectedResponse);

        // act
        AssignmentResponse response = assignmentService.getAssignmentById(assignmentId);

        // assert
        assertNotNull(response);
        assertEquals(expectedResponse.getId(), response.getId());
        assertEquals(expectedResponse.getTitle(), response.getTitle());
        assertEquals(expectedResponse.getFilePath(), response.getFilePath());
        assertEquals(expectedResponse.getCourseName(), response.getCourseName());
        assertEquals(expectedResponse.getMarks(), response.getMarks());
    }

    @Test
    void deleteAssignmentById_ValidRequest_ShouldDeleteAssignment() {
        // arrange
        String courseName = "course01";
        String courseDescription = "course description";
        long courseId = 1L;
        long assignmentId = 1L;
        double marks = 50.0;
        String assignmentTitle = "first assignment";
        String assignmentFileLocation = "c://files/assignmet01.pdf";

        Course course = Course.builder()
                .id(courseId)
                .courseName(courseName)
                .courseDescription(courseDescription)
                .build();
        Assignment assignment = Assignment.builder()
                .title(assignmentTitle)
                .fileLocation(assignmentFileLocation)
                .course(course)
                .totalMarks(marks)
                .build();

        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));

        // act
        assignmentRepository.deleteById(assignmentId);

        // assert
        verify(assignmentRepository).deleteById(assignmentId);
    }

    @Test
    void deleteAssignmentById_InValidRequest_ShouldNotDeleteAssignment() {
        // arrange
        long assignmentId = 1L;
        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(AssignmentNotFoundException.class, () -> {
            assignmentService.deleteAssignmentById(assignmentId);
        });
    }

    @Test
    void getSubmissionsByAssignmentId() {

        // arrange
        long submissionId = 1L;
        long assignmentId = 1L;
        long assignmentSubmissionId = 1L;
        long courseId = 1L;
        String assignmentTitle = "this is an assignment title";
        String courseName = "this is a course";
        String courseDescription = "this is a course description";
        String fileLocation = "c://java/assignment.txt";
        double obtainedMarks = 50.0;
        double total = 60.0;

        UserProfile userProfile = UserProfile
                .builder()
                .firstName("firstName")
                .build();

        AssignmentSubmissionResponse assignmentSubmissionResponse = AssignmentSubmissionResponse
                .builder()
                .submissionId(submissionId)
                .assignmentTitle(assignmentTitle)
                .student(userProfile)
                .fileLocation(fileLocation)
                .obtainedMarks(obtainedMarks)
                .totalMarks(total)
                .build();

        Course course = Course.builder()
                .id(courseId)
                .courseName(courseName)
                .courseDescription(courseDescription)
                .build();
        Assignment assignment = Assignment.builder()
                .title(assignmentTitle)
                .fileLocation(fileLocation)
                .course(course)
                .totalMarks(total)
                .build();

        User user = User.builder()
                .id(1L)
                .build();
        Profile profile = Profile.builder()
                .id(1L)
                .build();

        AssignmentSubmission assignmentSubmission = AssignmentSubmission
                .builder()
                .id(assignmentSubmissionId)
                .student(user)
                .fileLocation(fileLocation)
                .obtainedMarks(obtainedMarks)
                .assignment(assignment)
                .build();

        List<AssignmentSubmission> assignmentSubmissions = List.of(assignmentSubmission,assignmentSubmission);
        List<AssignmentSubmissionResponse> expectedResponse = List.of(assignmentSubmissionResponse,assignmentSubmissionResponse);


        when(assignmentRepository.findById(assignmentId)).thenReturn(Optional.of(assignment));
        when(traineesAssignmentSubmissionRepository.findAllByAssignment(assignment)).thenReturn(assignmentSubmissions);
        when(userMapper.profileToUserProfile(profile)).thenReturn(userProfile);

        // act

        List<AssignmentSubmissionResponse> response = assignmentService.getSubmissionsByAssignmentId(assignmentId);

        // assert
        assertEquals(expectedResponse.size(), response.size());

    }

    @Test
    void getSubmissionById() {


        // arrange
        long submissionId = 1L;
        long assignmentId = 1L;
        long assignmentSubmissionId = 1L;
        long courseId = 1L;
        String assignmentTitle = "this is an assignment title";
        String courseName = "this is a course";
        String courseDescription = "this is a course description";
        String fileLocation = "c://java/assignment.txt";
        double obtainedMarks = 50.0;
        double total = 60.0;


        Course course = Course.builder()
                .id(courseId)
                .courseName(courseName)
                .courseDescription(courseDescription)
                .build();
        Assignment assignment = Assignment.builder()
                .title(assignmentTitle)
                .fileLocation(fileLocation)
                .course(course)
                .totalMarks(total)
                .build();



        UserProfile userProfile = UserProfile
                .builder()
                .firstName("firstName")
                .build();

        Profile profile = Profile.builder().build();

        User user = User.builder()
                .id(1L)
                .profile(profile)
                .build();

        AssignmentSubmission assignmentSubmission = AssignmentSubmission
                .builder()
                .id(assignmentSubmissionId)
                .student(user)
                .fileLocation(fileLocation)
                .obtainedMarks(obtainedMarks)
                .assignment(assignment)
                .build();

        AssignmentSubmissionResponse expectedResponse = AssignmentSubmissionResponse
                .builder()
                .submissionId(submissionId)
                .assignmentTitle(assignmentTitle)
                .student(userProfile)
                .fileLocation(fileLocation)
                .obtainedMarks(obtainedMarks)
                .totalMarks(total)
                .build();

        when(traineesAssignmentSubmissionRepository.findById(submissionId)).thenReturn(Optional.of(assignmentSubmission));

        // act
        AssignmentSubmissionResponse response = assignmentService.getSubmissionById(submissionId);

        // assert
        assertNotNull(response);
        assertEquals(expectedResponse.getSubmissionId(), response.getSubmissionId());
        assertEquals(expectedResponse.getAssignmentTitle(), response.getAssignmentTitle());
        assertEquals(expectedResponse.getFileLocation(), response.getFileLocation());
        //assertEquals(expectedResponse.getStudent(), response.getStudent());
        assertEquals(expectedResponse.getTotalMarks(), response.getTotalMarks());
        assertEquals(expectedResponse.getObtainedMarks(), response.getObtainedMarks());




    }

    @Test
    void updateSubmission() {






    }

    @Test
    void submitAssignment() {
    }

    @Test
    void getSubmissionsByStudent() {
    }
}