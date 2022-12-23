package com.training.erp.repository;


import com.training.erp.entity.Assignment;
import com.training.erp.entity.AssignmentSubmission;
import com.training.erp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission,Long> {
    List<AssignmentSubmission> findAllByAssignment(Assignment assignment);

    boolean existsByStudentAndAssignment(User student, Assignment assignment);

    List<AssignmentSubmission> findAllByStudent(User user);
}
