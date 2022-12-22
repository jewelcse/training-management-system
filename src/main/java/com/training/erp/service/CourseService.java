package com.training.erp.service;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.model.request.CourseRequestDto;
import com.training.erp.model.request.CourseUpdateRequest;
import com.training.erp.model.response.CourseDetails;
import com.training.erp.model.response.CourseResponse;

import java.util.List;

public interface CourseService {
    CourseResponse save(CourseRequestDto request);
    boolean existsByCourse(String course_name);
    List<CourseResponse> getCourses();
    void updateCourse(CourseUpdateRequest request);
    void deleteCourseByCourseId(long courseId);
    CourseDetails getCourseById(long id);
    List<Course> getCoursesByBatch(Batch batch);
}
