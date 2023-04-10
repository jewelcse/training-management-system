package com.tms.service.serviceImpl;


import com.tms.entity.*;
import com.tms.repository.*;
import com.tms.service.*;
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
