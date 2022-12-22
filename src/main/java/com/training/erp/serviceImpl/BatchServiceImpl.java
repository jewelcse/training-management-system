package com.training.erp.serviceImpl;

import com.training.erp.entity.*;
import com.training.erp.exception.BatchNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.BatchRequestDto;
import com.training.erp.model.request.AssignUserRequest;
import com.training.erp.model.request.RemoveUserRequest;
import com.training.erp.model.response.*;
import com.training.erp.repository.*;
import com.training.erp.service.BatchService;
import com.training.erp.mapper.BatchMapper;
import com.training.erp.mapper.CourseMapper;
import com.training.erp.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BatchServiceImpl implements BatchService {
    @Autowired
    private BatchRepository batchRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BatchMapper batchMapper;
    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private UserMapper userManager;
    @Override
    public BatchResponse save(BatchRequestDto request) {
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
    public List<BatchResponse> getAllBatch() {
        return batchMapper.batchListToBatchResponseDtoList(batchRepository.findAll());
    }

    @Override
    public MessageResponse assignUserToBatch(AssignUserRequest request) throws BatchNotFoundException, UserNotFoundException {
        Batch batch = getBatch(request.getBatchId());
        User user = getUser(request.getUserId());
        Set<User> currentUsers = batch.getUsers();
        currentUsers.add(user);
        batch.setUsers(currentUsers);
        batchRepository.save(batch);
        return MessageResponse.builder()
                .message("SUCCESS!")
                .build();

    }



    @Override
    public MessageResponse removeUserFromBatch(RemoveUserRequest request) {
        Batch batch = getBatch(request.getBatchId());
        User user = getUser(request.getUserId());
        Set<User> currentUsers = batch.getUsers();
        currentUsers.remove(user);
        batch.setUsers(currentUsers);
        batchRepository.save(batch);
        return MessageResponse.builder()
                .message("SUCCESS!")
                .build();
    }


    @Override
    public BatchDetails getBatchById(long id){
        Optional<Batch> batch = batchRepository.findById(id);
        if(batch.isEmpty()){
            throw new BatchNotFoundException("BATCH NOT FOUND!");
        }
        List<Course> courses = batch.get().getCourses();
        Set<User> users = batch.get().getUsers();
        return BatchDetails.builder()
                .id(batch.get().getId())
                .batchName(batch.get().getBatchName())
                .batchDescription(batch.get().getBatchDescription())
                .startDate(batch.get().getStartDate())
                .endDate(batch.get().getEndDate())
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

    private Batch getBatch(long id){
        Optional<Batch> batch = batchRepository.findById(id);
        if (batch.isEmpty()){
            throw new BatchNotFoundException("BATCH NOT FOUND!");
        }
        return batch.get();
    }

    private User getUser(long id){
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()){
            throw new UserNotFoundException("USER NOT FOUND");
        }
        return user.get();
    }





}
