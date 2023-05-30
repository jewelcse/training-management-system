package com.training.erp.repository;

import com.training.erp.entity.assignments.Assignment;
import com.training.erp.entity.courses.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment,Long> {
    List<Assignment> findAllByCourse(Course course);
}
