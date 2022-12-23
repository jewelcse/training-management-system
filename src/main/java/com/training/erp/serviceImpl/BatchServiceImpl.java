package com.training.erp.serviceImpl;

import com.training.erp.entity.*;
import com.training.erp.exception.BatchNotFoundException;
import com.training.erp.exception.CourseNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.*;
import com.training.erp.model.response.*;
import com.training.erp.repository.*;
import com.training.erp.service.BatchService;
import com.training.erp.mapper.BatchMapper;
import com.training.erp.mapper.CourseMapper;
import com.training.erp.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final BatchMapper batchMapper;
    private final CourseMapper courseMapper;
    private final UserMapper userManager;

    @Override
    public BatchCreateResponse save(BatchCreateRequest request) {

        if (existsByBatchName(request.getBatchName())) {
            throw new RuntimeException("ALREADY EXISTS!");
        }

        //batch
        Batch batch = new Batch();
        batch.setBatchName(request.getBatchName());
        batch.setBatchDescription(request.getBatchDescription());
        batch.setStartDate(request.getStartDate());
        batch.setEndDate(request.getEndDate());
        //save
        Batch response = batchRepository.save(batch);
        //response
        return batchMapper.batchToBatchResponseDto(response);
    }

    @Override
    public BatchCreateResponse update(BatchUpdateRequest request) {
        //fetched
        Batch batch = getBatch(request.getId());
        //set
        batch.setBatchName(request.getBatchName());
        batch.setBatchDescription(request.getBatchDescription());
        batch.setStartDate(request.getStartDate());
        batch.setEndDate(request.getEndDate());

        //save
        Batch response = batchRepository.save(batch);
        //response
        return batchMapper.batchToBatchResponseDto(response);
    }

    @Override
    public List<BatchCreateResponse> getAllBatch() {
        return batchMapper.batchListToBatchResponseDtoList(batchRepository.findAll());
    }

    @Override
    public MessageResponse assignUserToBatch(AddUserToBatchRequest request) throws BatchNotFoundException, UserNotFoundException {
        Batch batch = getBatch(request.getBatchId());
        User user = getUser(request.getUserId());
        Set<User> users = batch.getUsers();
        users.add(user);
        batch.setUsers(users);
        batchRepository.save(batch);
        return MessageResponse.builder()
                .message("SUCCESS!")
                .build();

    }

    @Override
    public MessageResponse removeUserFromBatch(RemoveUserRequest request) {
        Batch batch = getBatch(request.getBatchId());
        User user = getUser(request.getUserId());
        Set<User> users = batch.getUsers();
        users.remove(user);
        batch.setUsers(users);
        batchRepository.save(batch);
        return MessageResponse.builder()
                .message("SUCCESS!")
                .build();
    }

    @Override
    public MessageResponse addCourseToBatch(AddCourseToBatchRequest request) {
        Batch batch = getBatch(request.getBatchId());
        Course course = getCourse(request.getCourseId());
        List<Course> courses = batch.getCourses();
        courses.add(course);
        batch.setCourses(courses);
        batchRepository.save(batch);
        return MessageResponse.builder()
                .message("SUCCESS!")
                .build();
    }

    @Override
    public MessageResponse removeCourseFromBatch(RemoveCourseRequest request) {
        Batch batch = getBatch(request.getBatchId());
        Course course = getCourse(request.getCourseId());
        List<Course> courses = batch.getCourses();
        courses.remove(course);
        batch.setCourses(courses);
        batchRepository.save(batch);
        return MessageResponse.builder()
                .message("SUCCESS!")
                .build();
    }


    @Override
    public BatchDetailsResponse getBatchById(long id) {
        Batch batch = getBatch(id);
        List<Course> courses = batch.getCourses();
        Set<User> users = batch.getUsers();
        return BatchDetailsResponse.builder()
                .id(batch.getId())
                .batchName(batch.getBatchName())
                .batchDescription(batch.getBatchDescription())
                .startDate(batch.getStartDate())
                .endDate(batch.getEndDate())
                .course(courseMapper.courseListToCourseResponseDtoList(courses))
                .users(userManager.usersToUserDetailsList(users))
                .build();
    }


    @Override
    public boolean existsByBatchName(String batch_name) {
        return batchRepository.existsByBatchName(batch_name);
    }

    @Override
    public void deleteBatchById(long batchId) {
        batchRepository.deleteById(batchId);
    }

    private Batch getBatch(long id) {
        Optional<Batch> batch = batchRepository.findById(id);
        if (batch.isEmpty()) {
            throw new BatchNotFoundException("BATCH NOT FOUND!");
        }
        return batch.get();
    }

    private Course getCourse(long id) {
        Optional<Course> course = courseRepository.findById(id);
        if (course.isEmpty()) {
            throw new CourseNotFoundException("COURSE NOT FOUND!");
        }
        return course.get();
    }

    private User getUser(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new UserNotFoundException("USER NOT FOUND");
        }
        return user.get();
    }
}
