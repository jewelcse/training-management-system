package com.tms.repository;

import com.tms.entity.Batch;
import com.tms.entity.Course;
import com.tms.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    List<Schedule> findAllByCourse(Course course);
    List<Schedule> findAllByBatch(Batch batch);
    List<Schedule> findAllByCourseAndBatch(Course course, Batch batch);
}
