package com.training.erp.serviceImpl;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.entity.Trainer;
import com.training.erp.entity.User;
import com.training.erp.exception.CourseNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.CourseCreateRequest;
import com.training.erp.model.request.CourseUpdateRequest;
import com.training.erp.repository.CourseRepository;
import com.training.erp.repository.TrainerRepository;
import com.training.erp.repository.UserRepository;
import com.training.erp.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TrainerRepository trainerRepository;
    @Override
    public void createCourse(CourseCreateRequest request) {
        Course course = new Course();
        course.setCourseName(request.getCourse_name());
        course.setCourseDescription(request.getCourse_description());
        courseRepository.save(course);
    }

    @Override
    public Course findByCourseName(String course_name) {
        return courseRepository.findByCourseName(course_name);
    }

    @Override
    public boolean existsByCourse(String course_name) {
        return courseRepository.existsByCourseName(course_name);
    }

    @Override
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    @Override
    public void updateCourse(CourseUpdateRequest request) throws CourseNotFoundException {
        Course course = courseRepository.findById(request.getCourse_id())
                        .orElseThrow(()-> new CourseNotFoundException("Course not found"));
        course.setId(course.getId());
        course.setCourseName(request.getCourse_name());
        course.setCourseDescription(request.getCourse_description());
        course.setTrainer(course.getTrainer());
        courseRepository.save(course);
    }

    @Override
    public List<Course> getCoursesByTrainerId(long trainerId) throws UserNotFoundException {
        Trainer trainer = trainerRepository.findById(trainerId)
                .orElseThrow(()-> new UserNotFoundException("Trainer not found"));
        return courseRepository.findAllByTrainer(trainer);
    }

    @Override
    public void deleteCourseByCourseId(long courseId) throws CourseNotFoundException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(()-> new CourseNotFoundException("Course not found"));
        courseRepository.delete(course);
    }

    @Override
    public Course getCourseByCourseId(long courseId) throws CourseNotFoundException {
        return courseRepository.findById(courseId)
                .orElseThrow(()-> new CourseNotFoundException("Course not found"));
    }

    @Override
    public void updateCourseTrainer(Course course) {
        courseRepository.save(course);
    }

    @Override
    public List<Course> getCoursesByBatch(Batch batch) {
        return courseRepository.getAllCoursesByBatch(batch.getId());
    }

    @Override
    public Trainer getTrainerProfileByCourse(Course course) {
        return trainerRepository.findById(course.getTrainer().getId())
                .orElseThrow(()-> new RuntimeException("Trainer not found"));
    }

    @Override
    public void removeTrainerFromCourse(Course course) {
        course.setTrainer(null);
        courseRepository.save(course);
    }
}
