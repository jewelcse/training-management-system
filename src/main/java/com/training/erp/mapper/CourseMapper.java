package com.training.erp.mapper;

import com.training.erp.entity.courses.Course;
import com.training.erp.model.response.CourseInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CourseMapper {

    public CourseInfo courseToCourseResponseDto(Course course){
        if (course == null) return null;
        return CourseInfo.builder()
                .courseName(course.getCourseName())
                .courseDescription(course.getCourseDescription())
                .build();
    }

    public List<CourseInfo> courseListToCourseResponseDtoList(List<Course> courses){
        List<CourseInfo> courseResponseList = new ArrayList<>();
        courses.forEach(course-> courseResponseList.add(courseToCourseResponseDto(course)));
        return courseResponseList;
    }
}
