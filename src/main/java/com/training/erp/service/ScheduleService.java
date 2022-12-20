package com.training.erp.service;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    void saveSchedule(Schedule schedule);
    List<Schedule> getAllScheduleByBatch(Batch batch);
    List<Schedule> getAllSchedule();
    List<Schedule> getSchedulesByCourseAndBatch(Course course, Batch batch);
}
