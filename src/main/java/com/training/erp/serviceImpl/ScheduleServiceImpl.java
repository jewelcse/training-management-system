package com.training.erp.serviceImpl;


import com.training.erp.entity.batches.Batch;
import com.training.erp.entity.courses.Course;
import com.training.erp.entity.Schedule;
import com.training.erp.repository.ScheduleRepository;
import com.training.erp.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleRepository scheduleRepository;
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

}
