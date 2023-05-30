package com.training.erp.service;

import com.training.erp.entity.batches.Batch;
import com.training.erp.entity.courses.Course;
import com.training.erp.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    void saveSchedule(Schedule schedule);
    List<Schedule> getAllSchedule();
    List<Schedule> getSchedulesByCourseAndBatch(Course course, Batch batch);
}
