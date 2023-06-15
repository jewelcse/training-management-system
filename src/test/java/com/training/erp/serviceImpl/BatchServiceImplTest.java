package com.training.erp.serviceImpl;

import com.training.erp.entity.batches.Batch;
import com.training.erp.entity.courses.Course;
import com.training.erp.entity.users.User;
import com.training.erp.mapper.BatchMapper;
import com.training.erp.mapper.CourseMapper;
import com.training.erp.mapper.UserMapper;
import com.training.erp.model.request.*;
import com.training.erp.model.response.*;
import com.training.erp.repository.BatchRepository;
import com.training.erp.repository.CourseRepository;
import com.training.erp.repository.UserRepository;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class BatchServiceImplTest {

    @Mock
    private BatchRepository batchRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private BatchMapper batchMapper;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BatchServiceImpl batchService;

    @BeforeEach
    void setUp() {

        batchService = new BatchServiceImpl(
                batchRepository,
                userRepository,
                courseRepository,
                batchMapper,courseMapper, userMapper
        );

    }

    @Test
    void save() {

        String batchName = "batch";
        String batchDescription = "batch description";

        BatchCreateRequest request = BatchCreateRequest
                .builder()
                .batchName(batchName)
                .batchDescription(batchDescription)
                .startDate(null)
                .endDate(null)
                .build();

        Batch batch = Batch.builder()
                .id(1L)
                .batchName(batchName)
                .batchDescription(batchDescription)
                .startDate(null)
                .endDate(null)
                .build();

        BatchCreateResponse expectedResponse = BatchCreateResponse
                .builder()
                .id(1L)
                .batchName(batchName)
                .batchDescription(batchDescription)
                .startDate(null)
                .endDate(null)
                .build();


        when(batchService.existsByBatchName(batchName)).thenReturn(false);
        when(batchRepository.save(any(Batch.class))).thenReturn(batch);
        when(batchMapper.batchToBatchResponseDto(batch)).thenReturn(expectedResponse);

        // act
        BatchCreateResponse response = batchService.save(request);

        // assert
        assertNotNull(response);
        assertEquals(expectedResponse.getBatchName(),response.getBatchName());
        assertEquals(expectedResponse.getBatchDescription(),response.getBatchDescription());
        assertEquals(expectedResponse.getStartDate(),response.getStartDate());
        assertEquals(expectedResponse.getEndDate(),response.getEndDate());
    }

    @Test
    void update() {

        String batchName = "batch";
        String batchDescription = "batch description";

        BatchUpdateRequest request = BatchUpdateRequest
                .builder()
                .id(1L)
                .batchName(batchName)
                .batchDescription(batchDescription)
                .startDate(null)
                .endDate(null)
                .build();

        Batch batch = Batch.builder()
                .id(1L)
                .batchName(batchName)
                .batchDescription(batchDescription)
                .startDate(null)
                .endDate(null)
                .build();

        BatchCreateResponse expectedResponse = BatchCreateResponse
                .builder()
                .id(1L)
                .batchName(batchName)
                .batchDescription(batchDescription)
                .startDate(null)
                .endDate(null)
                .build();

        when(batchRepository.findById(1L)).thenReturn(Optional.of(batch));
        when(batchRepository.save(any(Batch.class))).thenReturn(batch);
        when(batchMapper.batchToBatchResponseDto(batch)).thenReturn(expectedResponse);

        // act
        BatchCreateResponse response = batchService.update(request);

        // assert
        assertNotNull(response);
        assertEquals(expectedResponse.getBatchName(),response.getBatchName());




    }

    @Test
    void getAllBatch() {
        String batchName = "batch";
        String batchDescription = "batch description";
        Batch batch = Batch.builder()
                .id(1L)
                .batchName(batchName)
                .batchDescription(batchDescription)
                .startDate(null)
                .endDate(null)
                .build();
        BatchCreateResponse batchResponse = BatchCreateResponse
                .builder()
                .id(1L)
                .batchName(batchName)
                .batchDescription(batchDescription)
                .startDate(null)
                .endDate(null)
                .build();

        List<Batch> batches = List.of(batch,batch);
        List<BatchCreateResponse> expectedResponse = List.of(batchResponse,batchResponse);

        when(batchRepository.findAll()).thenReturn(batches);
        when(batchMapper.batchListToBatchResponseDtoList(batches)).thenReturn(expectedResponse);

        // act
        List<BatchCreateResponse> response = batchService.getAllBatch();

        // assert
        assertNotNull(response);
        assertEquals(expectedResponse.size(), response.size());

    }

    @Test
    void assignUserToBatch() {

        long batchId = 1L;
        long userId = 1L;
        String batchName = "batch";
        String batchDescription = "batch description";

        AddUserToBatchRequest request = AddUserToBatchRequest
                .builder()
                .batchId(batchId)
                .userId(userId)
                .build();


        Batch batch = Batch.builder()
                .id(1L)
                .batchName(batchName)
                .batchDescription(batchDescription)
                .startDate(null)
                .endDate(null)
                .build();

        User user = User.builder()
                .id(userId)
                .batch(batch)
                .build();

        MessageResponse expectedResponse = MessageResponse.builder()
                .message("assigned success!")
                .build();

        when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // act
         MessageResponse response = batchService.assignUserToBatch(request);

         // assert
        assertNotNull(request);
        assertEquals(expectedResponse.getMessage(),response.getMessage());
    }

    @Test
    void removeUserFromBatch() {
        long batchId = 1L;
        long userId = 1L;
        String batchName = "batch";
        String batchDescription = "batch description";

        RemoveUserRequest request = RemoveUserRequest
                .builder()
                .batchId(batchId)
                .userId(userId)
                .build();


        Batch batch = Batch.builder()
                .id(1L)
                .batchName(batchName)
                .batchDescription(batchDescription)
                .startDate(null)
                .endDate(null)
                .build();

        User user = User.builder()
                .id(userId)
                .batch(batch)
                .build();

        MessageResponse expectedResponse = MessageResponse.builder()
                .message("remove user!")
                .build();

        when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);

        // act
        MessageResponse response = batchService.removeUserFromBatch(request);

        // assert
        assertNotNull(request);
        assertEquals(expectedResponse.getMessage(),response.getMessage());




    }

    @Test
    void addCourseToBatch() {

        long batchId = 1L;
        long courseId = 3L;
        long userId = 1L;
        String batchName = "batch";
        String batchDescription = "batch description";


        AddCourseToBatchRequest request = AddCourseToBatchRequest
                .builder()
                .batchId(batchId)
                .courseId(courseId)
                .build();

        Course course = Course.builder()
                .id(1L)
                .build();

        Course course2 = Course.builder()
                .id(2L)
                .build();

        Course course3 = Course.builder()
                .id(3L)
                .build();

        List<Course> courses = List.of(course,course2);

        Batch batch = Batch.builder()
                .id(1L)
                .batchName(batchName)
                .batchDescription(batchDescription)
                .startDate(null)
                .endDate(null)
                .courses(courses)
                .build();


        MessageResponse expectedResponse = MessageResponse.builder()
                .message("success")
                .build();


        when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course3));
        when(batchRepository.save(batch)).thenReturn(batch);

        // act
        MessageResponse response = batchService.addCourseToBatch(request);

        // assert
        assertNotNull(response);
        assertEquals(expectedResponse.getMessage(),response.getMessage());


    }

    @Test
    void removeCourseFromBatch() {

        long batchId = 1L;
        long courseId = 3L;
        long userId = 1L;
        String batchName = "batch";
        String batchDescription = "batch description";


        RemoveCourseRequest request = RemoveCourseRequest
                .builder()
                .batchId(batchId)
                .courseId(courseId)
                .build();

        Course course = Course.builder()
                .id(1L)
                .build();

        Course course2 = Course.builder()
                .id(2L)
                .build();

        Course course3 = Course.builder()
                .id(3L)
                .build();

        List<Course> courses = List.of(course,course2);

        Batch batch = Batch.builder()
                .id(1L)
                .batchName(batchName)
                .batchDescription(batchDescription)
                .startDate(null)
                .endDate(null)
                .courses(courses)
                .build();


        MessageResponse expectedResponse = MessageResponse.builder()
                .message("success")
                .build();


        when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course2));
        when(batchRepository.save(batch)).thenReturn(batch);

        // act
        MessageResponse response = batchService.removeCourseFromBatch(request);

        // assert
        assertNotNull(response);
        assertEquals(expectedResponse.getMessage(),response.getMessage());
    }

    @Test
    void getBatchById() {
        long batchId = 1L;
        long courseId = 3L;
        long userId = 1L;
        String batchName = "batch";
        String batchDescription = "batch description";


        RemoveCourseRequest request = RemoveCourseRequest
                .builder()
                .batchId(batchId)
                .courseId(courseId)
                .build();

        Course course = Course.builder()
                .id(1L)
                .build();

        Course course2 = Course.builder()
                .id(2L)
                .build();

        List<Course> courses = List.of(course,course2);


        CourseInfo info = CourseInfo
                .builder().build();

        User user = User.builder().build();

        List<User> users = List.of(user,user);

        UserInfo userInfo = UserInfo.builder().build();
        List<UserInfo> usersInfo = List.of(userInfo,userInfo);


        List<CourseInfo> courseInfoList = List.of(info,info);

        Batch batch = Batch.builder()
                .id(1L)
                .batchName(batchName)
                .batchDescription(batchDescription)
                .startDate(null)
                .endDate(null)
                .courses(courses)
                .trainees(users)
                .build();


        BatchDetailsResponse expected = BatchDetailsResponse.builder()
                .id(1L)
                .courses(courseInfoList)
                .trainees(usersInfo)
                .build();

        when(batchRepository.findById(batchId)).thenReturn(Optional.of(batch));
        when(courseMapper.courseListToCourseResponseDtoList(courses)).thenReturn(courseInfoList);

        // act
        BatchDetailsResponse response = batchService.getBatchById(batchId);

        // assert
        assertNotNull(response);


    }

    @Test
    void existsByBatchName() {
        String batchName = "batchName";
        when(batchRepository.existsByBatchName(batchName)).thenReturn(true);
        // act
        boolean response = batchRepository.existsByBatchName(batchName);
        // assert
        assertTrue(response);
    }

    @Test
    void deleteBatchById() {
        long batchId = 1L;
        batchService.deleteBatchById(batchId);
        verify(batchRepository, times(1)).deleteById(eq(batchId));
    }
}