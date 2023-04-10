package com.tms.service;


import com.tms.entity.*;

import java.util.List;

public interface ScheduleService {
    void saveSchedule(Schedule schedule);
    List<Schedule> getAllSchedule();
    List<Schedule> getSchedulesByCourseAndBatch(Course course, Batch batch);
}
