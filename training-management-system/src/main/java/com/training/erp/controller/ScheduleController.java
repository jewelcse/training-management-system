package com.training.erp.controller;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.entity.Schedule;
import com.training.erp.entity.Trainer;
import com.training.erp.exception.BatchNotFoundException;
import com.training.erp.exception.CourseNotFoundException;
import com.training.erp.model.request.ScheduleRequest;
import com.training.erp.service.BatchService;
import com.training.erp.service.CourseService;
import com.training.erp.service.ScheduleService;
import com.training.erp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ScheduleController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;
    @Autowired
    private BatchService batchService;
    @Autowired
    private ScheduleService scheduleService;

    private static final SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Create schedule
    @PostMapping("/schedules")
    public ResponseEntity<?> createSchedule(@RequestBody ScheduleRequest request) throws CourseNotFoundException, BatchNotFoundException {

        // Get the batch, course, and also trainer
        Batch batch = batchService.getBatchById(request.getBatch_id()).get();
        Course course = courseService.getCourseByCourseId(request.getCourse_id());
        Trainer trainer = course.getTrainer();

        // Start_date will be greater than all the same batch's previous schedules end_date
        // Get the all schedules by course
        List<Schedule> batchesSchedule = scheduleService.getSchedulesByCourseAndBatch(course,batch);
        // Check if the trainer/course is available that time or not
        if (isNotValidSchedule(batchesSchedule,request.getStart_date().getTime())){
            return ResponseEntity.ok("Invalid Schedule for Time: start_time"+request.getStart_date() + " end_time" + request.getEnd_date());
        }
        // Create the schedule object
        Schedule schedule = new Schedule();
        schedule.setTitle(request.getTopic());
        schedule.setDescription(request.getTopic_description());
        schedule.setTrainer(trainer);
        schedule.setCourse(course);
        schedule.setBatch(batch);
        schedule.setStart(request.getStart_date());
        schedule.setEnd(request.getEnd_date());

        scheduleService.saveSchedule(schedule);

        return ResponseEntity.ok("Create success");
    }

    private boolean isNotValidSchedule(List<Schedule> schedules, long data){
        return schedules.stream().anyMatch(schedule -> schedule.getEnd().getTime() > data);
    }




    @GetMapping("/schedules/batches/{batch-id}")
    public ResponseEntity<List<Schedule>> getAllScheduleByBatch(@PathVariable("batch-id") long batchId) throws BatchNotFoundException {

        Batch batch = batchService.getBatchById(batchId)
                .orElseThrow(()-> new BatchNotFoundException("Batch not found"));

        return ResponseEntity.ok(scheduleService.getAllScheduleByBatch(batch));

    }

    @GetMapping("/schedules")
    public ResponseEntity<List<Schedule>> getSchedules(){
        return ResponseEntity.ok(scheduleService.getAllSchedule());
    }

}
