package com.training.erp.repository;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {
    Course findByCourseName(String course_name);
    boolean existsByCourseName(String course_name);
    List<Course> findAllByTrainer(Trainer trainer);
    @Query("SELECT course FROM Course course LEFT JOIN  course.batches batch WHERE batch.id = ?1")
    List<Course> getAllCoursesByBatch(Long id);
}
