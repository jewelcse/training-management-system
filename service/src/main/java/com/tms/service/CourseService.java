package com.tms.service;

import com.tms.dto.request.*;
import com.tms.dto.response.*;

import java.util.List;

public interface CourseService {
    CourseResponse save(CourseCreateRequest request);

    CourseResponse update(CourseUpdateRequest request);

    boolean existsByCourse(String course_name);

    List<CourseResponse> getCourses();

    void deleteCourseById(long courseId);

    CourseDetailsResponse getCourseById(long id);

    MessageResponse addTrainerToCourse(AddTrainerToCourseRequest request);

    MessageResponse removeTrainerFromCourse(RemoveTrainerRequest request);

}
