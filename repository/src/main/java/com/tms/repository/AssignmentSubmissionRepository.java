package com.tms.repository;


import com.tms.entity.Assignment;
import com.tms.entity.AssignmentSubmission;
import com.tms.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentSubmissionRepository extends JpaRepository<AssignmentSubmission,Long> {
    List<AssignmentSubmission> findAllByAssignment(Assignment assignment);

    boolean existsByStudentAndAssignment(User student, Assignment assignment);

    List<AssignmentSubmission> findAllByStudent(User user);
}
