package com.training.erp.serviceImpl;

import com.training.erp.entity.*;
import com.training.erp.exception.BatchNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.BatchRequestDto;
import com.training.erp.model.request.UserAssignRequestDto;
import com.training.erp.model.response.BatchFullProfileResponse;
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
    public void createNewBatch(BatchRequestDto request) {
        Batch batch = new Batch();
        batch.setBatchName(request.getBatch_name());
        batch.setBatchDescription(request.getBatch_description());
        batch.setStartDate(request.getStart_date());
        batch.setEndDate(request.getEnd_date());
        batchRepository.save(batch);
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
    public Optional<Batch> getBatchById(long batchId) throws BatchNotFoundException {
        return batchRepository.findById(batchId);
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
