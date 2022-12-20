package com.training.erp.repository;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findAllByCourse(Course course);
    List<Schedule> findAllByBatch(Batch batch);
    List<Schedule> findAllByCourseAndBatch(Course course, Batch batch);
}
