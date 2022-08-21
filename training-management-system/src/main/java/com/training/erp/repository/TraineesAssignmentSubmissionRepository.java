package com.training.erp.repository;


import com.training.erp.entity.Assignment;
import com.training.erp.entity.Trainee;
import com.training.erp.entity.TraineesAssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TraineesAssignmentSubmissionRepository extends JpaRepository<TraineesAssignmentSubmission,Long> {
    //TraineesAssignmentSubmission findByAssignment(Assignment assignment);
    List<TraineesAssignmentSubmission> findAllByAssignment(Assignment assignment);
    TraineesAssignmentSubmission findByTrainee(Trainee trainee);
    boolean existsByTraineeAndAssignment(Trainee trainee, Assignment assignment);
    List<TraineesAssignmentSubmission> findAllByTrainee(Trainee trainee);
}
