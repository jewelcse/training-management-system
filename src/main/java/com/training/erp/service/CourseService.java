package com.training.erp.service;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.entity.Trainer;
import com.training.erp.exception.CourseNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.CourseCreateRequest;
import com.training.erp.model.request.CourseUpdateRequest;

import java.util.List;

public interface CourseService {
    void createCourse(CourseCreateRequest request);
    Course findByCourseName(String course_name);
    boolean existsByCourse(String course_name);
    List<Course> getCourses();
    void updateCourse(CourseUpdateRequest request) throws UserNotFoundException, CourseNotFoundException;
    List<Course> getCoursesByTrainerId(long trainerId) throws UserNotFoundException;
    void deleteCourseByCourseId(long courseId) throws CourseNotFoundException;
    Course getCourseByCourseId(long courseId) throws CourseNotFoundException;
    void updateCourseTrainer(Course course);
    List<Course> getCoursesByBatch(Batch batch);
    Trainer getTrainerProfileByCourse(Course course);
    void removeTrainerFromCourse(Course course);
}
