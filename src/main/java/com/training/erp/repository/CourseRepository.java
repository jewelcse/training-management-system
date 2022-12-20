package com.training.erp.repository;

import com.training.erp.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    boolean existsByCourseName(String courseName);
//    List<Course> getAllCoursesByBatch(Long id);

}
