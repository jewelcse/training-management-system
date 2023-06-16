package com.training.erp.serviceImpl;

import com.training.erp.entity.courses.Course;
import com.training.erp.mapper.AssignmentMapper;
import com.training.erp.mapper.CourseMapper;
import com.training.erp.mapper.UserMapper;
import com.training.erp.model.request.CourseCreateRequest;
import com.training.erp.model.response.CourseInfo;
import com.training.erp.repository.AssignmentRepository;
import com.training.erp.repository.CourseRepository;
import com.training.erp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@SpringBootTest
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private AssignmentMapper assignmentMapper;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {

        courseService = new CourseServiceImpl(
                courseRepository,assignmentRepository,userRepository,
                courseMapper,assignmentMapper,userMapper
        );

    }

    @Test
    void save() {

        String courseName = "course name";
        String courseDescription =  "course description";

        CourseCreateRequest request = CourseCreateRequest.builder()
                .courseName(courseName)
                .courseDescription(courseDescription)
                .build();


        Course course = Course.builder()
                .courseName(courseName)
                .courseDescription(courseDescription)
                .build();

        Course course2 = Course.builder()
                .id(1L)
                .courseName(courseName)
                .courseDescription(courseDescription)
                .build();

        CourseInfo expectedResponse = CourseInfo.builder()
                .courseName(courseName)
                .courseDescription(courseDescription)
                .build();


        when(courseRepository.existsByCourseName(courseName)).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course2);
        when(courseMapper.courseToCourseResponseDto(course)).thenReturn(expectedResponse);

        // act
        CourseInfo response = courseService.save(request);

        // assert
        assertNotNull(response);
        assertEquals(expectedResponse.getCourseName(), response.getCourseName());
        assertEquals(expectedResponse.getCourseDescription(), response.getCourseDescription());


    }

    @Test
    void update() {
    }

    @Test
    void getCourses() {
    }

    @Test
    void getCourseById() {
    }

    @Test
    void addTrainerToCourse() {
    }

    @Test
    void removeTrainerFromCourse() {
    }

    @Test
    void deleteCourseById() {
    }

    @Test
    void existsByCourse() {
    }
}