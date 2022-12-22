package com.training.erp.mapper;

import com.training.erp.entity.Course;
import com.training.erp.model.response.CourseResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseMapper {

    public CourseResponse courseToCourseResponseDto(Course course){
        return CourseResponse.builder()
                .id(course.getId())
                .courseName(course.getCourseName())
                .courseDescription(course.getCourseDescription())
                .build();
    }

    public List<CourseResponse> courseListToCourseResponseDtoList(List<Course> courses){
        List<CourseResponse> courseResponseList = new ArrayList<>();
        courses.forEach(course-> courseResponseList.add(courseToCourseResponseDto(course)));
        return courseResponseList;
    }
}
