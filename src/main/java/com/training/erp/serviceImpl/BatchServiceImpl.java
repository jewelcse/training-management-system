package com.training.erp.serviceImpl;

import com.training.erp.entity.*;
import com.training.erp.exception.BatchNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.BatchRequestDto;
import com.training.erp.model.request.UserAssignRequestDto;
import com.training.erp.model.response.BatchDetailsDto;
import com.training.erp.model.response.BatchFullProfileResponse;
import com.training.erp.model.response.BatchResponseDto;
import com.training.erp.repository.*;
import com.training.erp.service.BatchService;
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
    private CourseRepository courseRepository;
    @Override
    public BatchResponseDto save(BatchRequestDto request) {
        //batch
        Batch batch = new Batch();
        batch.setBatchName(request.getBatchName());
        batch.setBatchDescription(request.getBatchDescription());
        batch.setStartDate(request.getStartDate());
        batch.setEndDate(request.getEndDate());
        batchRepository.save(batch);
        //response
        return BatchResponseDto.builder()
                .batchName(request.getBatchName())
                .batchDescription(request.getBatchDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();
    }

    @Override
    public List<Batch> getAllBatch() {
        return batchRepository.findAll();
    }

    @Override
    public void assignUserToBatch(UserAssignRequestDto request) throws BatchNotFoundException, UserNotFoundException {
        Batch batch = batchRepository.findById(request.getBatchId())
                .orElseThrow(()-> new BatchNotFoundException("Batch not found"));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new UserNotFoundException("User not found"));

    }

    @Override
    public BatchDetailsDto getBatchById(long batchId){
        Optional<Batch> batch = batchRepository.findById(batchId);
        Set<Course> courses = batch.get().getCourses();
        Set<User> users = batch.get().getUsers();
        return BatchDetailsDto.builder()
                .batchName(batch.get().getBatchName())
                .batchDescription(batch.get().getBatchDescription())
                .startDate(batch.get().getStartDate())
                .endDate(batch.get().getEndDate())
                .course(courses)
                .users(users)
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





    @Override
    public BatchFullProfileResponse getBatchFullProfileByBatch(Batch batch) {

        List<Course> courses = null;

        return new BatchFullProfileResponse(batch.getBatchName(),courses);
    }

    @Override
    public void updateBatch(Batch batch) {
        batchRepository.save(batch);
    }

    @Override
    public void removeCourseFromBatch(Batch batch, Course course) {
        batch.getCourses().remove(course);
        batchRepository.save(batch);

    }

}
