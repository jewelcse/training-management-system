package com.training.erp.service;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.exception.CourseNotFoundException;
import com.training.erp.exception.UserNotFoundException;
import com.training.erp.model.request.CourseRequestDto;
import com.training.erp.model.request.CourseUpdateRequest;
import com.training.erp.model.response.CourseResponseDto;

import java.util.List;

public interface CourseService {
    CourseResponseDto save(CourseRequestDto request);
    boolean existsByCourse(String course_name);
    List<Course> getCourses();
    void updateCourse(CourseUpdateRequest request) throws UserNotFoundException, CourseNotFoundException;
    void deleteCourseByCourseId(long courseId) throws CourseNotFoundException;
    Course getCourseByCourseId(long courseId) throws CourseNotFoundException;
    List<Course> getCoursesByBatch(Batch batch);
}
