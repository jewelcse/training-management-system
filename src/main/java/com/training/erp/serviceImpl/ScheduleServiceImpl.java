package com.training.erp.serviceImpl;


import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.entity.Schedule;
import com.training.erp.repository.ScheduleRepository;
import com.training.erp.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Override
    public void saveSchedule(Schedule schedule) {
        scheduleRepository.save(schedule);
    }
    @Override
    public List<Schedule> getAllSchedule() {
        return scheduleRepository.findAll();
    }


    @Override
    public List<Schedule> getSchedulesByCourseAndBatch(Course course, Batch batch) {
        return scheduleRepository.findAllByCourseAndBatch(course, batch);
    }

    @Override
    public List<Schedule> getAllScheduleByBatch(Batch batch) {
        return scheduleRepository.findAllByBatch(batch);
    }
}
