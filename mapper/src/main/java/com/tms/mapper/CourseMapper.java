package com.tms.mapper;

import com.tms.dto.response.*;
import com.tms.entity.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseMapper {

    public CourseResponse courseToCourseResponseDto(Course course){
        if (course == null) return null;
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
