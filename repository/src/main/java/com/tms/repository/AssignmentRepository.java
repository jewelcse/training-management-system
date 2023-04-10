package com.tms.repository;


import com.tms.entity.Assignment;
import com.tms.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment,Long> {
    List<Assignment> findAllByCourse(Course course);
}
