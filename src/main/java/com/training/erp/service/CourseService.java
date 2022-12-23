package com.training.erp.service;

import com.training.erp.entity.Batch;
import com.training.erp.entity.Course;
import com.training.erp.model.request.AddTrainerRequest;
import com.training.erp.model.request.CourseCreateRequest;
import com.training.erp.model.request.CourseUpdateRequest;
import com.training.erp.model.request.RemoveTrainerRequest;
import com.training.erp.model.response.CourseDetails;
import com.training.erp.model.response.CourseResponse;
import com.training.erp.model.response.MessageResponse;

import java.util.List;

public interface CourseService {
    CourseResponse save(CourseCreateRequest request);

    CourseResponse update(CourseUpdateRequest request);

    boolean existsByCourse(String course_name);

    List<CourseResponse> getCourses();

    void deleteCourseById(long courseId);

    CourseDetails getCourseById(long id);

    MessageResponse addTrainerToCourse(AddTrainerRequest request);

    MessageResponse removeTrainerFromCourse(RemoveTrainerRequest request);

}
