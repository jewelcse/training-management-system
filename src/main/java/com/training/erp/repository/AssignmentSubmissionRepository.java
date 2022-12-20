package com.training.erp.repository;


import com.training.erp.entity.Assignment;
import com.training.erp.entity.AssignmentSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission,Long> {
    //AssignmentSubmission findByAssignment(Assignment assignment);
    List<AssignmentSubmission> findAllByAssignment(Assignment assignment);
}
