package com.training.erp.serviceImpl;

import com.training.erp.entity.assignments.Assignment;
import com.training.erp.entity.courses.Course;
import com.training.erp.mapper.AssignmentMapper;
import com.training.erp.mapper.UserMapper;
import com.training.erp.model.request.AssignmentCreateRequest;
import com.training.erp.model.response.AssignmentResponse;
import com.training.erp.repository.AssignmentRepository;
import com.training.erp.repository.AssignmentSubmissionRepository;
import com.training.erp.repository.CourseRepository;
import com.training.erp.repository.UserRepository;
import com.training.erp.service.AssignmentService;
import com.training.erp.util.FilesStorageService;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

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
    void save() {
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
        when(assignmentMapper.assignmentToAssignmentResponse(assignment)).thenReturn(expectedResponse );
        // Act

        AssignmentResponse response = assignmentService.save(request);

        // Assert
        //assertNotNull(response);
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
    void getAssignments() {
    }

    @Test
    void getAssignmentsByCourse() {
    }

    @Test
    void getAssignmentById() {
    }

    @Test
    void deleteAssignmentById() {
    }

    @Test
    void getSubmissionsByAssignmentId() {
    }

    @Test
    void getSubmissionById() {
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