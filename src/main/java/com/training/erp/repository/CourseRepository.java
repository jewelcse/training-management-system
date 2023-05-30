package com.training.erp.repository;

import com.training.erp.entity.courses.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    boolean existsByCourseName(String courseName);

}
