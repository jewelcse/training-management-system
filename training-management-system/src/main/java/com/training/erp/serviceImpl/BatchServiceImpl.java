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
    private TraineeRepository traineeRepository;
    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private AssignmentRepository assignmentRepository;

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

        if (user.getRoles().stream().anyMatch(role -> role.getName().toString().equals("ROLE_TRAINEE"))){
            Trainee trainee = traineeRepository.findByUserId(user.getId());
            trainee.setBatch(batch);
            trainee.setId(trainee.getId());
            traineeRepository.save(trainee);
        }else if(user.getRoles().stream().anyMatch(role -> role.getName().toString().equals("ROLE_TRAINER"))){
            Trainer trainer = trainerRepository.findByUserId(user.getId());
            Set<Batch> batches = trainer.getBatches();
            batches.add(batch);
            trainer.setBatches(batches);
            trainer.setId(trainer.getId());
            trainerRepository.save(trainer);
        }
    }

    @Override
    public Optional<Batch> getBatchById(long batchId) throws BatchNotFoundException {
        return batchRepository.findById(batchId);
    }

    @Override
    public Batch getBatchByTrainee(Trainee trainee) throws BatchNotFoundException {
        return batchRepository.findById(trainee.getBatch().getId())
                .orElseThrow(()-> new BatchNotFoundException("Batch not found"));
    }

    @Override
    public List<Batch> getBatchesByTrainerId(Long trainerId) {
        return batchRepository.findAllByTrainer(trainerId);
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
    public boolean existsById(long batchId) {
        return batchRepository.existsById(batchId);
    }

    @Override
    public void assignTraineeToBatch(Batch batch, Trainee trainee) throws BatchNotFoundException {
        trainee.setBatch(batch);
        traineeRepository.save(trainee);
    }

    @Override
    public void assignTrainerToBatch(Batch batch, Trainer trainer) {
        batch.getTrainers().add(trainer);
        trainerRepository.save(trainer);
    }

    @Override
    public BatchFullProfileResponse getBatchFullProfileByBatch(Batch batch) {

        List<Course> courses = courseRepository.getAllCoursesByBatch(batch.getId());
        List<Trainee> trainees = traineeRepository.findAllByBatch(batch);
        List<Trainer> trainers = trainerRepository.getAllTrainersByBatch(batch.getId());

        return new BatchFullProfileResponse(batch.getBatchName(),courses,trainees,trainers);
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

    @Override
    public void removeTrainerFromBatch(Batch batch, Trainer trainer) {
        batch.getTrainers().remove(trainer);
        batchRepository.save(batch);
    }

    @Override
    public void removeTraineeFromBatch(Batch batch, Trainee trainee) {
        trainee.setBatch(null);
        traineeRepository.save(trainee);
    }

}
