package com.training.erp.service;

import com.training.erp.model.request.AddTrainerToCourseRequest;
import com.training.erp.model.request.CourseCreateRequest;
import com.training.erp.model.request.CourseUpdateRequest;
import com.training.erp.model.request.RemoveTrainerRequest;
import com.training.erp.model.response.CourseDetailsResponse;
import com.training.erp.model.response.CourseInfo;
import com.training.erp.model.response.MessageResponse;

import java.util.List;

public interface CourseService {
    CourseInfo save(CourseCreateRequest request);

    CourseInfo update(CourseUpdateRequest request);

    boolean existsByCourse(String course_name);

    List<CourseInfo> getCourses();

    void deleteCourseById(long courseId);

    CourseDetailsResponse getCourseById(long id);

    MessageResponse addTrainerToCourse(AddTrainerToCourseRequest request);

    MessageResponse removeTrainerFromCourse(RemoveTrainerRequest request);

}
